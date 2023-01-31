package com.example.myfitness

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitness.DataAccessObjects.ExercisesDAO
import com.example.myfitness.DataAccessObjects.UsersDAO
import com.example.myfitness.R
import com.example.myfitness.adapters.DailyWorkoutAdapter
import com.example.myfitness.adapters.PlanAdapter
import com.example.myfitness.entities.*
import com.example.myfitness.helpers.NewGenerateProgramHelper
import com.example.myfitness.utils.Notification
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.security.Timestamp
import java.util.Calendar
import java.util.Date


class PlanFragment : Fragment() {
    //Kreiranje potrebnih varijabli koje će se kasnije inicijalizirati
    private lateinit var btnGenerate: FloatingActionButton
    private lateinit var btnShowDailyPlan: FloatingActionButton
    private lateinit var temp: TextView
    private lateinit var recyclerView: RecyclerView

    var listAllDays = listOf("Ponedjeljak", "Utorak", "Srijeda", "Četvrtak", "Petak", "Subota", "Nedjelja")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Prikazivanje sadržaja fragmenta
        return inflater.inflate(R.layout.fragment_plan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Dodijeljivanje vrijednosti prethodno kreiranim varijablama
        btnGenerate = view.findViewById(R.id.id_fragment_profile_add_program)
        btnShowDailyPlan = view.findViewById(R.id.id_pregledaj_dnevni_plan)
        recyclerView = view.findViewById(R.id.rv_plan_main)
        temp = view.findViewById(R.id.id_test)
        loadPlan()
        btnGenerate.setOnClickListener { showDialog() }

        btnShowDailyPlan.setOnClickListener { showPlanDialog() }



    }


    private fun loadPlan() {
        lifecycleScope.launch {
            val currentUser = UsersDAO.getCurrentUser(requireContext())
            val planList: MutableList<Plan> = ExercisesDAO.getPlan(currentUser) //Dohvaćanja najnovijeg plana iz baze za ulogiranog korisnika
            if(!planList.isNullOrEmpty()) { //Ako plan postoji učitavanje u recycler view
                val planAdapter = PlanAdapter(planList)

                recyclerView.adapter = planAdapter
                recyclerView.layoutManager = LinearLayoutManager(view?.context)
                temp.text = "Prethodno generirani trening"
            }
        }
    }

    private fun loadWorkout(datumiranje : String) {
        //dohvaca trenutnog korisnika i sprema ga u varijablu currentUser
        //lifecycleScope omogućuje pisanje asinkrono
        lifecycleScope.launch {
            val currentUser = UsersDAO.getCurrentUser(requireContext())
            //dohvaca listu DailyExercises sa određenim datumom pomocu getDaily, plan treninga za određeni datum
            val dailyList : MutableList<DailyExercises> = ExercisesDAO.getDaily(currentUser, datumiranje)
            //Log.d("napisanitekst", dailyList[0].exerciseName)
            //ako lista nije prazna ili null kreira se instanca DailyWorkoutAdapter i salje mu se lista dailyList - prikazuje listu u recycler viewu
            if(!dailyList.isNullOrEmpty()) {
                val dailyWorkoutAdapter = DailyWorkoutAdapter(dailyList)

                //postavlja dailyWorkoutAdapter kao adapter za recyclerView kao linear layout manager
                recyclerView.adapter = dailyWorkoutAdapter
                recyclerView.layoutManager = LinearLayoutManager(view?.context)
                temp.text = "Prethodno generirani trening"
            }else{
                Toast.makeText(context, "Ne postoje vježbe kreirane za taj datum!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    //biraj datum za koji zelis dohvatit trening
    private fun showPlanDialog() {

        //instanca layouta koji ce se moći inflateati (inflatea se kao sadrđaj u Alert Dialogu)
        val showDailyWorkoutHelper = LayoutInflater.from(context).inflate(R.layout.show_daily_exercises, null)

        //stvara alert dijalog sa positive buttonom koji klikom

        AlertDialog.Builder(context)
            .setView(showDailyWorkoutHelper)
            .setTitle("Pretražite dnevne treninge")
            .setPositiveButton("Unesi datum") {_, _ ->
                val uneseniDatum = showDailyWorkoutHelper.findViewById<EditText>(R.id.date_picker).text.toString()   //dohvati input korisnika
                val pattern = "^\\d{2}\\.\\d{2}\\.\\d{4}.\$"

                //provjerava da li je input korisnika bio točno unesen
                if(uneseniDatum.isEmpty()) {
                    Toast.makeText(context, "Morate upisati neki datum!", Toast.LENGTH_SHORT).show()
                } else if(!uneseniDatum.matches(pattern.toRegex())){
                    Toast.makeText(context, "Unesite ispravan datum u formatu dd.mm.yyyy.", Toast.LENGTH_SHORT).show()
                } else{
                    loadWorkout(uneseniDatum)
                }

            }
            .show()

    }



    private fun showDialog() {
        //Kreiranje inflatera za generiranje programa i odabiranje dana
        val newGenerateProgramView = LayoutInflater.from(context).inflate(R.layout.generate_program, null)
        val dialogHelperPlan = NewGenerateProgramHelper(newGenerateProgramView)

        val newSelectDaysView = LayoutInflater.from(context).inflate(R.layout.generate_program_pick_days, null)
        val dialogHelperDays = NewGenerateProgramHelper(newSelectDaysView)

        //Inicijalizacija potrebnih varijabli u alertDialog-u
        val service = Notification(requireContext().applicationContext)
        val days = arrayOf("Ponedjeljak", "Utorak", "Srijeda", "Četvrtak", "Petak", "Subota", "Nedjelja")
        val bul = booleanArrayOf(false, false, false, false, false, false, false)
        var listSelectedDays = mutableListOf<String>()
        var increment = 0;

        //Prvi alertDialog za odabiranje preferenci
        AlertDialog.Builder(context)
            .setView(newGenerateProgramView)
            .setTitle("Generiranje plana treninga")
            .setPositiveButton("Odaberi dane") {_, _ ->
                var newPlanPreferences = dialogHelperPlan.buildPlan()
                if(newPlanPreferences.days != 7) {
                    //Drugi alertDialog za odabiranje dana koji se pali ako je korisnik odabrao manje od 7 dana
                    AlertDialog.Builder(context)
                        .setView(newSelectDaysView)
                        .setMultiChoiceItems(days, bul) { _, item: Int, checked: Boolean ->
                            if(checked) {
                                if(increment < newPlanPreferences.days) {
                                    increment++
                                    if(!listSelectedDays.contains(days[item]))
                                        listSelectedDays.add(days[item]) //Spremanje odabranog dana u listu dana
                                }
                                else {
                                    increment++
                                    bul[item] = false //Obavještavanje korisnika da ne može odabrati više dana
                                    Toast.makeText(context, "Ne možete odabrati više dana!", Toast.LENGTH_SHORT).show()
                                }
                            }
                            else {
                                increment--
                                if(listSelectedDays.contains(days[item]))
                                    listSelectedDays.remove(days[item]) //Uklanjanje odabranog dana iz liste dana
                            }
                        }
                        .setPositiveButton("Generiraj plan treninga") {_, _ ->
                            //Sortiranje odabranih dana, generiranje plana za odabrane dane i prikazivanje generiranog plana u recylerview
                            val sortedListDays = listSelectedDays.sortedWith(compareBy {days.indexOf(it)})
                            determinePlan(newPlanPreferences, sortedListDays as MutableList<String>)
                            recyclerView.layoutManager = LinearLayoutManager(view?.context)
                            service.showNotification(sortedListDays)
                        }.show()
                }
                else {
                    //Ako je broj dana 7 odmah generira plan treninga sa obrzirom na odabrane preference
                    determinePlan(newPlanPreferences)
                    recyclerView.layoutManager = LinearLayoutManager(view?.context)
                    service.showNotification(listAllDays)
                }
            }.show()

        //Prikazivanje vrijednosti u spinner na alertDialogu
        dialogHelperPlan.populateSpinnerPreference()
        dialogHelperPlan.populateSpinnerExperience()
        dialogHelperPlan.populateSpinnerDays()
    }

    private fun determinePlan(newPlanPreferences: PlanPreferences, listSelectedDays: MutableList<String> = mutableListOf()) {
        //Provjera koliko dana korisnik želi trenirati te generiranja programa za toliko dana
        when(newPlanPreferences.days) {
            1 -> generateFullBody(newPlanPreferences, listSelectedDays)
            2 -> generateUpperLower(newPlanPreferences, listSelectedDays)
            3 -> generatePPL(newPlanPreferences, listSelectedDays)
            4 -> generatePPSL(newPlanPreferences, listSelectedDays)
            5 -> generateBroSplit(newPlanPreferences, listSelectedDays)
            6 -> generate2xPPL(newPlanPreferences, listSelectedDays)
            7 -> generateBroUpperLower(newPlanPreferences)
        }
    }

    private fun generateBroUpperLower(newPlanPreferences: PlanPreferences) {
        //Generiranje programa za 7 dana
        lifecycleScope.launch {
            val finalList: MutableList<Plan> = arrayListOf()
            val difficulty = getExperience(newPlanPreferences.experience) //Određivanje iskustva
            //Dohvaćanje vježbi iz baze
            val selectedExercisesChest = combineList(getList("Prsa", 5, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val selectedExercisesBack = combineList(getList("Leđa", 5, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val selectedExcersisesShoulder = combineList(getList("Ramena", 5, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val selectedExercisesLegs = combineList(getList("Noge", 5, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val selectedExercisesArms = combineList(getList("Bicepsi", 3, difficulty), getList("Tricepsi", 3, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val selectedExercisesUpper = combineList(getList("Prsa", 2, difficulty), getCardio(newPlanPreferences.preference), getList("Leđa", 2, difficulty), getList("Ramena", 1, difficulty), getList("Bicepsi", 1, difficulty), getList("Tricepsi", 1, difficulty), preference = newPlanPreferences.preference)
            val selectedExercisesLower = combineList(getList("Noge", 5, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            //Spremanje vježbi po određenim danima u listu
            finalList.add(Plan("Ponedjeljak", selectedExercisesChest))
            finalList.add(Plan("Utorak", selectedExercisesBack))
            finalList.add(Plan("Srijeda", selectedExcersisesShoulder))
            finalList.add(Plan("Četvrtak", selectedExercisesLegs))
            finalList.add(Plan("Petak", selectedExercisesArms))
            finalList.add(Plan("Subota", selectedExercisesUpper))
            finalList.add(Plan("Nedjelja", selectedExercisesLower))
            var currentUser = UsersDAO.getCurrentUser(requireContext())
            //Pohranjivanje plana u bazu
            ExercisesDAO.addPlan(finalList, currentUser)
            val planAdapter = PlanAdapter(finalList)

            //Prikazivanje plana u recyclerview
            recyclerView.adapter = planAdapter
            temp.text = "Bro Split x Upper Lower"
        }
    }

    private fun generate2xPPL(newPlanPreferences: PlanPreferences, listSelectedDays: MutableList<String>) {
        //Generiranje programa za 6 dana
        lifecycleScope.launch {
            val finalList: MutableList<Plan> = arrayListOf()
            val tempDays = mutableListOf<String>()
            tempDays.addAll(listAllDays)
            val difficulty = getExperience(newPlanPreferences.experience) //Određivanje iskustva
            //Dohvaćanje vježbi iz baze
            val selectedExercisesPush1 = combineList(getList("Prsa", 3, difficulty), getList("Ramena", 1, difficulty), getList("Tricepsi", 1, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val selectedExercisesPull1 = combineList(getList("Leđa", 4, difficulty), getList("Bicepsi", 1, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val selectedExercisesLegs1 = combineList(getList("Noge", 5, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val selectedExercisesPush2 = combineList(getList("Prsa", 2, difficulty), getList("Ramena", 2, difficulty), getList("Tricepsi", 2, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val selectedExercisesPull2 = combineList(getList("Leđa", 3, difficulty), getList("Bicepsi", 4, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val selectedExercisesLegs2 = combineList(getList("Noge", 5, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val listRest: MutableList<Exercises> = arrayListOf()
            listRest.add(Exercises("Odmor"))

            //Spremanje dohvaćenih vježbi u završnu listu po danima
            //Npr. odabrani dani su Pon, Uto, Sri, Čet, Sub i Ned
            //Određivanje o kojem se danu radi kako bi za drugi dan u nizu mogao spremiti druge vježbe
            var increment = 1
            for(day in listSelectedDays) {
                when(increment) {
                    1 -> finalList.add(Plan(day, selectedExercisesPush1))
                    2 -> finalList.add(Plan(day, selectedExercisesPull1))
                    3 -> finalList.add(Plan(day, selectedExercisesLegs1))
                    4 -> finalList.add(Plan(day, selectedExercisesPush2))
                    5 -> finalList.add(Plan(day, selectedExercisesPull2))
                    6 -> finalList.add(Plan(day, selectedExercisesLegs2))
                }
                increment++
                //Uklanjanje dana za koje već postoje vježbe kako bi za ne iskorištene dane stavili odmor
                tempDays.remove(day)
            }
            for(day in tempDays) {
                finalList.add(Plan(day, listRest))
            }
            //sortiranje liste po danima
            val sortedFinalList = finalList.sortedWith(compareBy {listAllDays.indexOf(it.day)}) as MutableList

            //Spremanje liste u bazu
            var currentUser = UsersDAO.getCurrentUser(requireContext())
            ExercisesDAO.addPlan(sortedFinalList, currentUser)
            val planAdapter = PlanAdapter(sortedFinalList)

            //Učitavanje liste iz baze
            recyclerView.adapter = planAdapter
            temp.text = "2xPush Pull Legs"
        }
    }

    private fun generateBroSplit(newPlanPreferences: PlanPreferences, listSelectedDays: MutableList<String>) {
        //Generiranje programa za 5 dana
        //Koncept je isti kao za generiranje programa za 6 dana
        //Samo što se dohvaćaju drugačije vježbe
        lifecycleScope.launch {
            val finalList: MutableList<Plan> = arrayListOf()
            val tempDays = mutableListOf<String>()
            tempDays.addAll(listAllDays)
            val difficulty = getExperience(newPlanPreferences.experience)
            val selectedExercisesChest = combineList(getList("Prsa", 5, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val selectedExercisesBack = combineList(getList("Leđa", 5, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val selectedExcersisesShoulder = combineList(getList("Ramena", 5, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val selectedExercisesLegs = combineList(getList("Noge", 5, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val selectedExercisesArms = combineList(getList("Bicepsi", 3, difficulty), getList("Tricepsi", 3, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val listRest: MutableList<Exercises> = arrayListOf()
            listRest.add(Exercises("Odmor"))

            var increment = 1
            for(day in listSelectedDays) {
                when(increment) {
                    1 -> finalList.add(Plan(day, selectedExercisesChest))
                    2 -> finalList.add(Plan(day, selectedExercisesBack))
                    3 -> finalList.add(Plan(day, selectedExcersisesShoulder))
                    4 -> finalList.add(Plan(day, selectedExercisesLegs))
                    5 -> finalList.add(Plan(day, selectedExercisesArms))
                }
                increment++
                tempDays.remove(day)
            }
            for(day in tempDays) {
                finalList.add(Plan(day, listRest))
            }
            val sortedFinalList = finalList.sortedWith(compareBy {listAllDays.indexOf(it.day)}) as MutableList

            var currentUser = UsersDAO.getCurrentUser(requireContext())
            ExercisesDAO.addPlan(sortedFinalList, currentUser)
            val planAdapter = PlanAdapter(sortedFinalList)

            recyclerView.adapter = planAdapter
            temp.text = "Bro Split"
        }
    }

    private fun generatePPSL(newPlanPreferences: PlanPreferences, listSelectedDays: MutableList<String>) {
        //Generiranje programa za 4 dana
        //Koncept je isti kao za generiranje programa za 5 i 6 dana
        //Samo što se dohvaćaju drugačije vježbe
        lifecycleScope.launch {
            val finalList: MutableList<Plan> = arrayListOf()
            val tempDays = mutableListOf<String>()
            tempDays.addAll(listAllDays)
            val difficulty = getExperience(newPlanPreferences.experience)
            val selectedExercisesPush = combineList(getList("Prsa", 4, difficulty), getList("Tricepsi", 2, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val selectedExercisesPull = combineList(getList("Leđa", 4, difficulty), getList("Bicepsi", 2, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val selectedExcersisesShoulder = combineList(getList("Ramena", 4, difficulty), getList("Bicepsi", 1, difficulty), getList("Tricepsi", 1, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val selectedExercisesLegs = combineList(getList("Noge", 5, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val listRest: MutableList<Exercises> = arrayListOf()
            listRest.add(Exercises("Odmor"))

            var increment = 1
            for(day in listSelectedDays) {
                when(increment) {
                    1 -> finalList.add(Plan(day, selectedExercisesPush))
                    2 -> finalList.add(Plan(day, selectedExercisesPull))
                    3 -> finalList.add(Plan(day, selectedExcersisesShoulder))
                    4 -> finalList.add(Plan(day, selectedExercisesLegs))
                }
                increment++
                tempDays.remove(day)
            }
            for(day in tempDays) {
                finalList.add(Plan(day, listRest))
            }
            val sortedFinalList = finalList.sortedWith(compareBy {listAllDays.indexOf(it.day)}) as MutableList

            var currentUser = UsersDAO.getCurrentUser(requireContext())
            ExercisesDAO.addPlan(sortedFinalList, currentUser)
            val planAdapter = PlanAdapter(sortedFinalList)

            recyclerView.adapter = planAdapter
            temp.text = "Push Pull Shoulders Legs"
        }
    }

    private fun generatePPL(newPlanPreferences: PlanPreferences, listSelectedDays: MutableList<String>) {
        //Generiranje programa za 3 dana
        //Koncept je isti kao za generiranje programa za 4, 5 i 6 dana
        //Samo što se dohvaćaju drugačije vježbe
        lifecycleScope.launch {
            val finalList: MutableList<Plan> = arrayListOf()
            val tempDays = mutableListOf<String>()
            tempDays.addAll(listAllDays)
            val difficulty = getExperience(newPlanPreferences.experience)
            val selectedExercisesPush = combineList(getList("Prsa", 2, difficulty), getList("Ramena", 2, difficulty), getList("Tricepsi", 2, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val selectedExercisesPull = combineList(getList("Leđa", 4, difficulty), getList("Bicepsi", 2, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val selectedExercisesLegs: List<Exercises> = combineList(getList("Noge", 5, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val listRest: MutableList<Exercises> = arrayListOf()
            listRest.add(Exercises("Odmor"))

            var increment = 1
            for(day in listSelectedDays) {
                when(increment) {
                    1 -> finalList.add(Plan(day, selectedExercisesPush))
                    2 -> finalList.add(Plan(day, selectedExercisesPull))
                    3 -> finalList.add(Plan(day, selectedExercisesLegs))
                }
                increment++
                tempDays.remove(day)
            }
            for(day in tempDays) {
                finalList.add(Plan(day, listRest))
            }
            val sortedFinalList = finalList.sortedWith(compareBy {listAllDays.indexOf(it.day)}) as MutableList

            var currentUser = UsersDAO.getCurrentUser(requireContext())
            ExercisesDAO.addPlan(sortedFinalList, currentUser)
            val planAdapter = PlanAdapter(sortedFinalList)

            recyclerView.adapter = planAdapter
            temp.text = "Push Pull Legs"
        }
    }

    private fun generateUpperLower(newPlanPreferences: PlanPreferences, listSelectedDays: MutableList<String>) {
        //Generiranje programa za 2 dana
        //Koncept je isti kao za generiranje programa za 3, 4, 5 i 6 dana
        //Samo što se dohvaćaju drugačije vježbe
        lifecycleScope.launch {
            val finalList: MutableList<Plan> = arrayListOf()
            val tempDays = mutableListOf<String>()
            tempDays.addAll(listAllDays)
            val difficulty = getExperience(newPlanPreferences.experience)
            val selectedExercisesUpper = combineList(getList("Prsa", 2, difficulty), getList("Leđa", 2, difficulty), getList("Ramena", 1, difficulty), getList("Bicepsi", 1, difficulty), getList("Tricepsi", 1, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val selectedExercisesLower = combineList(getList("Noge", 6, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val listRest: MutableList<Exercises> = arrayListOf()
            listRest.add(Exercises("Odmor"))

            var increment = 1
            for(day in listSelectedDays) {
                when(increment) {
                    1 -> finalList.add(Plan(day, selectedExercisesUpper))
                    2 -> finalList.add(Plan(day, selectedExercisesLower))
                }
                increment++
                tempDays.remove(day)
            }
            for(day in tempDays) {
                finalList.add(Plan(day, listRest))
            }
            val sortedFinalList = finalList.sortedWith(compareBy {listAllDays.indexOf(it.day)}) as MutableList

            var currentUser = UsersDAO.getCurrentUser(requireContext())
            ExercisesDAO.addPlan(sortedFinalList, currentUser)
            val planAdapter = PlanAdapter(sortedFinalList)

            recyclerView.adapter = planAdapter
            temp.text = "Upper Lower"
        }
    }

    private fun generateFullBody(newPlanPreferences: PlanPreferences, listSelectedDays: MutableList<String>) {
        //Generiranje programa za 1 dan
        //Koncept je isti kao za generiranje programa za 2, 3, 4, 5 i 6 dana
        //Samo što se dohvaćaju drugačije vježbe
        lifecycleScope.launch {
            val finalList: MutableList<Plan> = arrayListOf()
            val tempDays = mutableListOf<String>()
            tempDays.addAll(listAllDays)
            val difficulty = getExperience(newPlanPreferences.experience)
            val selectedExercisesFinal = combineList(getList("Prsa", 2, difficulty), getList("Leđa", 2, difficulty), getList("Ramena", 2, difficulty), getList("Noge", 2, difficulty), getList("Bicepsi", 1, difficulty), getList("Tricepsi", 1, difficulty), getCardio(newPlanPreferences.preference), preference = newPlanPreferences.preference)
            val listRest: MutableList<Exercises> = arrayListOf()
            listRest.add(Exercises("Odmor"))

            for(day in listSelectedDays) {
                finalList.add(Plan(day, selectedExercisesFinal))
                tempDays.remove(day)
            }
            for(day in tempDays) {
                finalList.add(Plan(day, listRest))
            }
            val sortedFinalList = finalList.sortedWith(compareBy {listAllDays.indexOf(it.day)}) as MutableList

            var currentUser = UsersDAO.getCurrentUser(requireContext())
            ExercisesDAO.addPlan(sortedFinalList, currentUser)
            val planAdapter = PlanAdapter(sortedFinalList)

            recyclerView.adapter = planAdapter
            temp.text = "Full body"
        }
    }

    private fun getCardio(preference: String): List<Exercises> {
        val listCardio : MutableList<Exercises> = ArrayList()
        when(preference) {
            "Mršavljenje" -> listCardio.add(Exercises("Cardio 30min"))
            "Općenito zdravlje" -> listCardio.add(Exercises("Cardio 15min"))
        }
        return listCardio
    }

    private fun getExperience(experience: String): Int {
        //Određivanje težine vježbi koje je korisnik odabrao kako bi ih mogli dohvatiti iz baze
        when(experience) {
            "Beginner" -> return 1
            "Intermediate" -> return 2
            "Advanced" -> return 3
        }
        return 1
    }

    private suspend fun getList(bodyPart: String, n: Int, difficulty: Int): List<Exercises> {
        //Dohvaćanje vježbi
        when(bodyPart) {
            "Prsa" -> return reduceList(getListDAO(bodyPart, difficulty), n)
            "Leđa" -> return reduceList(getListDAO(bodyPart, difficulty), n)
            "Ramena" -> return reduceList(getListDAO(bodyPart, difficulty), n)
            "Noge" -> return reduceList(getListDAO(bodyPart, difficulty), n)
            "Bicepsi" -> return reduceList(getListDAO(bodyPart, difficulty), n)
            "Tricepsi" -> return reduceList(getListDAO(bodyPart, difficulty), n)
        }
        return ArrayList()
    }

    private suspend fun getListDAO(bodyPart: String, difficulty: Int): MutableList<Exercises> {
        //Dohvaćanje vježbi iz baze podataka po određenom dijelu tijela i težini
        return ExercisesDAO.getExercise(bodyPart, difficulty)
    }

    private fun reduceList(list: MutableList<Exercises>, n: Int): List<Exercises> {
        //Odabiranje određenog broja nasumičnih vježbi iz liste
        return list.asSequence().shuffled().take(n).toList()
    }

    private fun combineList(vararg lists: List<Exercises>, preference: String): List<Exercises> {
        //Spajanje više listi u jednu
        var result: List<Exercises> = ArrayList()
        for(list in lists) {
            result += addSetsReps(preference, list as MutableList<Exercises>)
        }
        return result
    }

    private fun addSetsReps(goal: String, list: MutableList<Exercises>): List<Exercises> {
        //Određivanje cilja korisnika te prosljeđivanje na funkciju koja će za taj cilj
        //Dodati serije i ponavljanja
        when(goal) {
            "Izgradnja mišića" -> return(addMuscle(list))
            "Mršavljenje" -> return(loseWeight(list))
            "Općenito zdravlje" -> return(overallHealth(list))
        }
        return ArrayList()
    }

    private fun overallHealth(lists: MutableList<Exercises>): List<Exercises> {
        //Spremanje random broja za serije i ponavljanja u svaki element liste
        //Serije i ponavljanaj su bazirana po istraživanjima koja su pokazala
        //Količinu rada vježbi kako bi se postigao neki cilj
        var result: List<Exercises> = ArrayList()
        for(list in lists) {
            if(list.exercise != "Cardio 15min") {
                list.exercise += " " + (2..3).random() + "x" + (12..16).random()
                result += list
            }
            else
                result += list
        }
        return result
    }

    private fun loseWeight(lists: MutableList<Exercises>): List<Exercises> {
        //Isto kao gornja fukcija samo drugačiji brojevi za serije i ponavljanja
        var result: List<Exercises> = ArrayList()
        for(list in lists) {
            if(list.exercise != "Cardio 30min") {
                list.exercise += " " + (3..4).random() + "x" + (10..12).random()
                result += list
            }
            else
                result += list
        }
        return result
    }

    private fun addMuscle(lists: MutableList<Exercises>): List<Exercises> {
        //Isto kao gornja fukcija samo drugačiji brojevi za serije i ponavljanja
        var result: List<Exercises> = ArrayList()
        for(list in lists) {
            list.exercise += " " + (2..4).random() + "x" + (6..8).random()
            result += list
        }
        return result
    }
}