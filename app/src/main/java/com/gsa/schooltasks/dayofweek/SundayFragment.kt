package com.gsa.schooltasks.dayofweek

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.gsa.schooltasks.DialogSolvedJob
import com.gsa.schooltasks.JobAddActivity
import com.gsa.schooltasks.JobViewModel
import com.gsa.schooltasks.R
import com.gsa.schooltasks.database.Job
import com.gsa.schooltasks.database.Subject
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "SundayFragment"

private const val EXTRA_SUBJECT_NAME = "com.gsa.schooltasks.subject_name"

class SundayFragment : Fragment() {

    var sdf: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy")

    private lateinit var subjSundayRecyclerView : RecyclerView
    private var adapter: SundayFragment.SubjSundayAdapter? = SubjSundayAdapter(emptyList())

    val jobViewModel: JobViewModel by lazy {
        ViewModelProvider(this, defaultViewModelProviderFactory).get(JobViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sunday, container, false)

        subjSundayRecyclerView = view.findViewById(R.id.subj_sunday_recycler_view) as RecyclerView
        subjSundayRecyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jobViewModel.getSubjectsForSunday().observe(viewLifecycleOwner, Observer {
                subjects -> subjects?.let {

            Log.i(TAG,"В воскресенье ${subjects.size} предметов")
            updateUI(subjects)
        }
        })

    }

    // холдер для одного предмета в списке
    private inner class SubjSundayHolder (view: View) : RecyclerView.ViewHolder(view),View.OnClickListener {

        private lateinit var subject: Subject

        private var cardForSubjectSchedule : MaterialCardView = itemView.findViewById(R.id.card_for_subj_sched) as MaterialCardView

        val itemShedSubjName: TextView = itemView.findViewById(R.id.item_shed_subj_name)
        //val itemShedSubjJob: TextView = itemView.findViewById(R.id.item_shed_subj_job)
        var itemRecyclerView: RecyclerView = itemView.findViewById(R.id.item_shed_subj_list_job)
        //val itemShedSubjJob: TextView = itemView.findViewById(R.id.item_shed_subj_job)
        private var adapter: SubjSundayHolder.ItemRecyclerAdapter? = ItemRecyclerAdapter(emptyList())

        fun buildListJobs(nameSubject: String){
            jobViewModel.getNoSolvedJobsLiveData(nameSubject).observe(viewLifecycleOwner, Observer {
                    jobs -> jobs?.let {
                itemUpdateUI(jobs)

                if (jobs.size == 0) {
                    cardForSubjectSchedule.setBackgroundColor(resources.getColor(R.color.colorNormal))
                } else {
                    cardForSubjectSchedule.setBackgroundColor(resources.getColor(R.color.colorAccent))
                }
            }
            })
        }

        private inner class ItemRecyclerViewHolder (view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

            private lateinit var job: Job

            val itemShedJobContent: TextView = itemView.findViewById(R.id.item_shed_job_content)
            val itemShedJobDate: TextView = itemView.findViewById(R.id.item_shed_job_date)

            init {
                itemView.setOnClickListener(this)
            }

            fun bind(job: Job) {
                this.job = job
                itemShedJobContent.text=job.contentJob
                itemShedJobDate.text=sdf.format(job.dateJob)

            }

            override fun onClick(v: View?) {
                val dialogSolvedJob = DialogSolvedJob (job)
                val bundle = Bundle ()
                dialogSolvedJob.arguments = bundle


                val fragmentTransaction = Objects.requireNonNull<FragmentManager>(parentFragmentManager).beginTransaction()
                dialogSolvedJob.show(fragmentTransaction, dialogSolvedJob.TAG)
            }
        }

        private inner class ItemRecyclerAdapter (var jobs: List<Job>) : RecyclerView.Adapter<ItemRecyclerViewHolder> () {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): ItemRecyclerViewHolder {
                val view = layoutInflater.inflate(R.layout.list_item_job_for_shedule,parent,false)
                return ItemRecyclerViewHolder(view)
            }

            override fun onBindViewHolder(holder: ItemRecyclerViewHolder, position: Int) {
                val subject = jobs[position]
                holder.bind(subject)
            }

            override fun getItemCount(): Int {
                return jobs.size
            }
        }

        private fun itemUpdateUI(jobs: List<Job>){
            adapter = ItemRecyclerAdapter(jobs)
            itemRecyclerView.adapter = adapter
        }

        init {
            itemView.setOnClickListener(this)
        }

        fun bind (subject: Subject) {
            this.subject = subject
            itemShedSubjName.text = subject.nameSubject
            buildListJobs(subject.nameSubject)
            itemRecyclerView.layoutManager = LinearLayoutManager(context)

        }


        override fun onClick(v: View?) {

            val intent = Intent(activity, JobAddActivity::class.java)
            intent.putExtra(EXTRA_SUBJECT_NAME,subject.nameSubject)
            startActivity(intent)

        }

    }

    // адаптер для полного списка предметов
    private inner class SubjSundayAdapter (var subjects: List<Subject>): RecyclerView.Adapter<SubjSundayHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjSundayHolder {
            val view = layoutInflater.inflate(R.layout.list_item_subject_shedule,parent,false)
            return SubjSundayHolder(view)
        }

        override fun onBindViewHolder(holder: SubjSundayHolder, position: Int) {
            val subject = subjects[position]
            holder.bind(subject)
        }

        override fun getItemCount(): Int {
            return subjects.size
        }

    }

    private fun updateUI(subjects: List<Subject>){
        adapter = SubjSundayAdapter(subjects)
        subjSundayRecyclerView.adapter = adapter
    }

}