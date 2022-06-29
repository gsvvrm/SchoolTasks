package com.gsa.schooltasks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.gsa.schooltasks.database.Subject

private const val TAG = "SubjectsListActivity"

class SubjectsListActivity : AppCompatActivity() {

    private lateinit var fabAddNewSubject: ExtendedFloatingActionButton
    private lateinit var subjectRecyclerView: RecyclerView

    private var adapter: SubjectAdapter? = SubjectAdapter(emptyList())

    val jobViewModel: JobViewModel by lazy {
        ViewModelProvider(this, defaultViewModelProviderFactory).get(JobViewModel::class.java)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subjects_list)

        fabAddNewSubject = findViewById(R.id.fab_add_subject)
        fabAddNewSubject.setOnClickListener {
            val intent = Intent (this, SubjectEditorActivity::class.java)
            startActivity(intent)
        }

        subjectRecyclerView = findViewById(R.id.subject_recycler_view)
        subjectRecyclerView.layoutManager = LinearLayoutManager(this)


        jobViewModel.subjectListLiveData.observe(this,
            Observer { subjects -> subjects.let {
                updateUI(subjects)
            } })

    }

    // холдер для одного предмета в списке
    private inner class SubjectHolder (view:View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var subject: Subject

        val itemSubjectName: TextView = itemView.findViewById(R.id.item_subject_name)
        val itemSubjectMonday: TextView = itemView.findViewById(R.id.item_subject_monday)
        val itemSubjectTuesday: TextView = itemView.findViewById(R.id.item_subject_tuesday)
        val itemSubjectWednesday: TextView = itemView.findViewById(R.id.item_subject_wednesday)
        val itemSubjectThursday: TextView = itemView.findViewById(R.id.item_subject_thursday)
        val itemSubjectFriday: TextView = itemView.findViewById(R.id.item_subject_friday)
        val itemSubjectSaturday: TextView = itemView.findViewById(R.id.item_subject_saturday)
        val itemSubjectSunday: TextView = itemView.findViewById(R.id.item_subject_sunday)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind (subject: Subject) {
            this.subject = subject
            itemSubjectName.text = subject.nameSubject
            itemSubjectMonday.isVisible = subject.onMondaySubj
            itemSubjectTuesday.isVisible = subject.onTuesdaySubj
            itemSubjectWednesday.isVisible = subject.onWednesdaySubj
            itemSubjectThursday.isVisible = subject.onThursdaySubj
            itemSubjectFriday.isVisible = subject.onFridaySubj
            itemSubjectSaturday.isVisible = subject.onSaturdaySubj
            itemSubjectSunday.isVisible = subject.onSundaySubj

        }

        override fun onClick(v: View?) {

           var sel = subject.nameSubject
            Log.d(TAG, "$sel")

            val intent = SubjectEditorActivity.newIntent(this@SubjectsListActivity,subject.nameSubject)
            startActivity(intent)

        }


    }

    //адаптер для получения списка
    private inner class SubjectAdapter (var subjects: List<Subject>) : RecyclerView.Adapter<SubjectHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectHolder {
            val view = layoutInflater.inflate(R.layout.list_item_subject,parent,false)
            return SubjectHolder(view)
        }

        override fun onBindViewHolder(holder: SubjectHolder, position: Int) {
            val subject = subjects[position]
            holder.bind(subject)
        }

        override fun getItemCount(): Int {
            return subjects.size
        }
    }

    private fun updateUI (subjects: List<Subject>) {
        adapter = SubjectAdapter(subjects)
        subjectRecyclerView.adapter = adapter
    }
}