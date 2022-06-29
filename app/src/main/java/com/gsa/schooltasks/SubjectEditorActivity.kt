package com.gsa.schooltasks

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gsa.schooltasks.database.Subject

private const val TAG = "SubjectEditorActivity"

private const val EXTRA_NAME_SUBJECT = "com.gsa.schooltasks.name_subject"

class SubjectEditorActivity : AppCompatActivity() {

    private lateinit var subject: Subject

    private var isExistSubject: Boolean = false

    private lateinit var nameSubjectTextEditor: EditText
    private lateinit var setMondayCheck: CheckBox
    private lateinit var setTuesdayCheck: CheckBox
    private lateinit var setWednesdayCheck: CheckBox
    private lateinit var setThursdayCheck: CheckBox
    private lateinit var setFridayCheck: CheckBox
    private lateinit var setSaturdayCheck: CheckBox
    private lateinit var setSundayCheck: CheckBox
    private lateinit var btnAddSubjectCheck: Button
    private lateinit var btnDellSubjectCheck: Button


    val jobViewModel: JobViewModel by lazy {
        ViewModelProvider(this, defaultViewModelProviderFactory).get(JobViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_editor)

        nameSubjectTextEditor = findViewById(R.id.subject_name)
        setMondayCheck = findViewById(R.id.set_monday)
        setTuesdayCheck = findViewById(R.id.set_tuesday)
        setWednesdayCheck = findViewById(R.id.set_wednsday)
        setThursdayCheck = findViewById(R.id.set_thursday)
        setFridayCheck = findViewById(R.id.set_friday)
        setSaturdayCheck = findViewById(R.id.set_saturday)
        setSundayCheck = findViewById(R.id.set_sunday)
        btnAddSubjectCheck = findViewById(R.id.btn_add_subject)
        btnDellSubjectCheck = findViewById(R.id.btn_delete_subject)

        btnAddSubjectCheck.isEnabled=false


        var nameSubject = intent.getStringExtra(EXTRA_NAME_SUBJECT).toString()
        Log.d(TAG, "передача интента $nameSubject")

        //получение предмета и загрузка предмета в эдитор
        jobViewModel.loadSubject(nameSubject)

        //обновление эдитора
        jobViewModel.subjectLiveData.observe(this, androidx.lifecycle.Observer {
                subject -> subject?.let {
            this.subject = subject
            updateUI()
        }
        })

        //проверка существования предмета
        jobViewModel.isNameSubjRowIsExist(nameSubject).observe(this, Observer {
                isExist -> isExist?.let {
            this.isExistSubject = isExist
            Log.d(TAG,"isExistSubject = $isExistSubject")
        }
        })

        //блокировка кнопки удаления если заданий менее 1
        jobViewModel.getCountSubjects().observe(this, Observer {
            countSubj -> countSubj?.let {
                btnDellSubjectCheck.isEnabled = countSubj != 1 or 0
        }
        })

        blockAddButton()
        // кнопка добавления или сохранения изменений предмета
        btnAddSubjectCheck.setOnClickListener {
            if (isExistSubject) {
                saveSubject()
            } else {
                addSubject()
            }
            //закрытие окна редактирования предмета
            closeActivity()
        }


        // кнопка удаления предмета
        btnDellSubjectCheck.setOnClickListener {
            if (isExistSubject){
                deleteSubject()
                closeActivity()
            } else {
                closeActivity()
            }


        }
    }

    companion object{
        fun newIntent(packageContext: Context, nameSubject: String): Intent {
            return Intent(packageContext, SubjectEditorActivity::class.java).apply {
                putExtra(EXTRA_NAME_SUBJECT, nameSubject)
            }
        }
    }

    private fun updateUI(){
        nameSubjectTextEditor.setText(subject.nameSubject)
        setMondayCheck.isChecked = subject.onMondaySubj
        setTuesdayCheck.isChecked = subject.onTuesdaySubj
        setWednesdayCheck.isChecked = subject.onWednesdaySubj
        setThursdayCheck.isChecked = subject.onThursdaySubj
        setFridayCheck.isChecked = subject.onFridaySubj
        setSaturdayCheck.isChecked = subject.onSaturdaySubj
        setSundayCheck.isChecked = subject.onSundaySubj

    }

    private fun addSubject () {
        subject = Subject(0,
            nameSubjectTextEditor.getText().toString(),
            setMondayCheck.isChecked,
            setTuesdayCheck.isChecked,
            setWednesdayCheck.isChecked,
            setThursdayCheck.isChecked,
            setFridayCheck.isChecked,
            setSaturdayCheck.isChecked,
            setSundayCheck.isChecked)

        jobViewModel.addSubject(subject)
    }

    private fun saveSubject(){
        subject = Subject(subject.idSubject,
            nameSubjectTextEditor.getText().toString(),
            setMondayCheck.isChecked,
            setTuesdayCheck.isChecked,
            setWednesdayCheck.isChecked,
            setThursdayCheck.isChecked,
            setFridayCheck.isChecked,
            setSaturdayCheck.isChecked,
            setSundayCheck.isChecked)

       jobViewModel.updateSubject(subject)
    }

    private fun deleteSubject() {
        subject = Subject(subject.idSubject,
            nameSubjectTextEditor.getText().toString(),
            setMondayCheck.isChecked,
            setTuesdayCheck.isChecked,
            setWednesdayCheck.isChecked,
            setThursdayCheck.isChecked,
            setFridayCheck.isChecked,
            setSaturdayCheck.isChecked,
            setSundayCheck.isChecked)

        jobViewModel.deleteSubject(subject)
    }

    private fun blockAddButton (){

        //блокировка кнопки добавления предмета при отсутствии названия
        nameSubjectTextEditor.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val length = s.length
                if (length == 0) {
                    btnAddSubjectCheck.isEnabled = false
                } else if (length > 0) {
                    btnAddSubjectCheck.isEnabled = true
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

    }

    private fun closeActivity(){
        this.finish()
    }
}