package com.gsa.schooltasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gsa.schooltasks.database.Job
import java.util.*
import android.text.Editable

import android.text.TextWatcher




private const val TAG = "JobAddActivity"

private const val EXTRA_SUBJECT_NAME = "com.gsa.schooltasks.subject_name"

class JobAddActivity : AppCompatActivity(), OnItemSelectedListener {

    private lateinit var job: Job

    private lateinit var spinnerSubjects : Spinner
    private lateinit var editTextContentJob: EditText
    private lateinit var buttonAddJob: Button

    private var subjectName: String = ""

    private val calendar: Calendar = Calendar.getInstance()

    val jobViewModel: JobViewModel by lazy {
        ViewModelProvider(this, defaultViewModelProviderFactory).get(JobViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_add)

        Log.d(TAG,"активность создана")

        spinnerSubjects = findViewById(R.id.spinner_subjects)
        editTextContentJob = findViewById(R.id.edit_text_content_job)
        buttonAddJob = findViewById(R.id.button_add_job)

        jobViewModel.subjectNameListLiveData.observe(this, Observer {
            subjectsList -> subjectsList.let {
               Log.d(TAG,"Список $subjectsList")

            //адаптер для спиннера предметов
            val adapter: ArrayAdapter<String> =
                ArrayAdapter<String>(this, R.layout.spinner_item, subjectsList)


            var index = subjectsList.indexOf(intent.getStringExtra(EXTRA_SUBJECT_NAME))

            spinnerSubjects.adapter = adapter

            spinnerSubjects.setSelection(index)

            spinnerSubjects.onItemSelectedListener = this

        }
        })

        buttonAddJob.isEnabled = false


        editTextContentJob.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val length = s.length
                if (length == 0) {
                    buttonAddJob.isEnabled = false
                } else if (length > 0) {
                    buttonAddJob.isEnabled = true
                }

            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int,
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int,
            ) {

            }
        })

        //действие на нажатие кнопки
        buttonAddJob.setOnClickListener {
            //добавление предмета
            addJob()
            //закрыте окна добавления предмета
            closeActivity()
        }

    }

    //обработка выбора в спиннере предметов
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        subjectName = parent?.getItemAtPosition(position).toString()
    }

    //обработка выбора в спиннере предметов
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    //функция добавления задания
    private fun addJob(){
        job = Job(UUID.randomUUID(),
            subjectName,
            editTextContentJob.text.toString(),
            calendar.time,
            false)

        jobViewModel.addJob(job)
    }

    //функция закрытия активности
    private fun closeActivity(){
        this.finish()
    }


}