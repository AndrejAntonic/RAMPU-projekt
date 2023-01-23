package com.example.myfitness.helpers

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myfitness.DataAccessObjects.ExerciseDAO
import com.example.myfitness.R
import com.google.firebase.firestore.FirebaseFirestore
import com.example.myfitness.entities.Exercise


class AddExerciseDialog(private val activity: Activity, val dialog: AlertDialog, private val context: Context):AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val bodyParts = arrayOf("Leđa", "Prsa", "Noge", "Ramena", "Bicepsi", "Tricepsi")
    private val difficulties = arrayOf("1", "2", "3", "4", "5")
    private val equipments = arrayOf("Bučice", "Girje", "Bench", "Smith mašina", "Sprava")
    private val PICK_IMAGE = 100
    private var pickedPhoto: Uri? = null
    private var pickedBitMap: Bitmap? = null
    //lateinit var imageView: ImageView


    init {
        val addExerciseView =
            LayoutInflater.from(context).inflate(R.layout.add_exercise_input_dialog, null)
        dialog.setContentView(addExerciseView)

        //imageView = addExerciseView.findViewById(R.id.addExerciseImageView)

        val spinnerDiff = addExerciseView.findViewById<Spinner>(R.id.difficultySpinner)

        val difficultyAdapter =
            ArrayAdapter(context, android.R.layout.simple_spinner_item, difficulties)
        difficultyAdapter.setDropDownViewResource(R.layout.spinner_item_basic)
        spinnerDiff.adapter = difficultyAdapter

        val spinnerEquip = addExerciseView.findViewById<Spinner>(R.id.equipmentSpinner)
        val equipmentAdapter =
            ArrayAdapter(context, android.R.layout.simple_spinner_item, equipments)
        equipmentAdapter.setDropDownViewResource(R.layout.spinner_item_basic)
        spinnerEquip.adapter = equipmentAdapter

        val spinnerBodyPart = addExerciseView.findViewById<Spinner>(R.id.bodyPartSpinner)
        val bodyPartAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, bodyParts)
        bodyPartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBodyPart.adapter = bodyPartAdapter

        spinnerBodyPart.setSelection(0)
        spinnerDiff.setSelection(0)
        spinnerEquip.setSelection(0)

        val addExerciseBtn = addExerciseView.findViewById<Button>(R.id.addExerciseBtn)
        addExerciseBtn.setOnClickListener {

            val exerciseName =
                addExerciseView.findViewById<EditText>(R.id.exerciseNameEditText).text.toString()
            val exerciseDescription =
                addExerciseView.findViewById<EditText>(R.id.exerciseDescriptionEditText).text.toString()
            val exerciseBodyPart = spinnerBodyPart.selectedItem.toString()
            val difficulty = spinnerDiff.selectedItem.toString().toInt()
            val equipment = spinnerEquip.selectedItem.toString()
//          imageView.setImageResource()
//            if (imageView.drawable != null) {
//                val bitmap = (imageView.drawable as BitmapDrawable).bitmap
//                val exercise = Exercise(
//                    exerciseName,
//                    exerciseDescription,
//                    difficulty,
//                    equipment,
//                    exerciseBodyPart
//                )

                val exercise = Exercise(
                    exerciseName,
                    exerciseDescription,
                    difficulty,
                    equipment,
                    exerciseBodyPart
                )

                ExerciseDAO.addExercise(exercise, db)
                Toast.makeText(context, "Vježba dodana!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
//            } else {
//                Toast.makeText(context, "Odaberite sliku!", Toast.LENGTH_SHORT).show()
//            }
        }

        val closeButton = addExerciseView.findViewById<Button>(R.id.closeBtn)
        closeButton.setOnClickListener {
            dialog.dismiss()
        }
    }
//        val selectImageBtn = addExerciseView.findViewById<Button>(R.id.addImageBtn)
//        selectImageBtn.setOnClickListener {
//            imageView= addExerciseView.findViewById(R.id.addExerciseImageView)
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//            activity.startActivityForResult(intent, PICK_IMAGE)
//        }

//        //val selectImageView = addExerciseView.findViewById<ImageView>(R.id.addExerciseImageView)
//        imageView.setOnClickListener{
//            openGallery()
//            println("NAKON PALJENAJ")
//        }
//
//    }
//
//    fun openGallery () {
////        val intent = Intent(Intent.ACTION_GET_CONTENT)
////        intent.type = "image/jpg"
////        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
////        println("INTENT " + intent)
////
////        startForResult.launch(intent)
//
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        activity.startActivityForResult(intent, PICK_IMAGE)
//    }

//    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            result: ActivityResult ->
//        println("TU JE ")
//        if (result.resultCode == Activity.RESULT_OK) {
//            val intent = result.data
//            println("REZULTAT ADJKLSH")
//            // Handle the Intent
//            //do stuff here
//        }
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
//            imageUri = data?.data
//            imageView.setImageURI(imageUri)
//        }
//    }

//    fun pickPhoto(view: View){
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
//        != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
//            1)
//        } else {
//            val galeriIntext = Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(galeriIntext,2)
//        }
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == 1){
//            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                val galeriIntext = Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI)
//                startActivityForResult(galeriIntext,2)
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        println("RESULT")
//        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
//            pickedPhoto = data.data
//            if (pickedPhoto != null) {
//                if (Build.VERSION.SDK_INT >= 28) {
//                    val source = ImageDecoder.createSource(this.contentResolver, pickedPhoto!!)
//                    pickedBitMap = ImageDecoder.decodeBitmap(source)
//                    imageView.setImageBitmap(pickedBitMap)
//                } else {
//                    pickedBitMap = MediaStore.Images.Media.getBitmap(this.contentResolver, pickedPhoto)
//                    imageView.setImageBitmap(pickedBitMap)
//                }
//            }
//            super.onActivityResult(requestCode, resultCode, data)
//        }
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        println("USAO U ONACTIVITYRESULT")
//        super.onActivityResult(requestCode, resultCode, data)
//        println("PROSAO SUPER")
//        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
//            println("USAO U IF")
//            if (data != null) {
//                println("DATA NIJE NULL DATA JE " + data)
//                val uri = data.data
//                println("URI JE" + uri)
//                imageView.setImageURI(uri)
//            }
//        }
//    }

}


