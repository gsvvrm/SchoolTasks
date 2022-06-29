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
import com.gsa.schooltasks.*
import com.gsa.schooltasks.database.Job
import com.gsa.schooltasks.database.Subject
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "MondayFragment"

private const val EXTRA_SUBJECT_NAME = "com.gsa.schooltasks.subject_name"

class MondayFragment : Fragment() {

   var sdf: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy")

        private lateinit var subjMondayRecyclerView : RecyclerView
    private var adapter: MondayFragment.SubjMondayAdapter? = SubjMondayAdapter(emptyList())

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
        val view = inflater.inflate(R.layout.fragment_monday, container, false)



        subjMondayRecyclerView = view.findViewById(R.id.subj_monday_recycler_view) as RecyclerView
        subjMondayRecyclerView.layoutManager = LinearLayoutManager(context)




        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jobViewModel.getSubjectsForMonday().observe(viewLifecycleOwner, Observer {
            subjects -> subjects?.let {

            Log.i(TAG,"В понедельник ${subjects.size} предметов")
            updateUI(subjects)
        }
        })

    }

    // холдер для одного предмета в списке
    private inner class SubjMondayHolder (view: View) : RecyclerView.ViewHolder(view),View.OnClickListener {

        private lateinit var subject: Subject

        private var cardForSubjectSchedule : MaterialCardView = itemView.findViewById(R.id.card_for_subj_sched) as MaterialCardView

        val itemShedSubjName: TextView = itemView.findViewById(R.id.item_shed_subj_name)
        //val itemShedSubjJob: TextView = itemView.findViewById(R.id.item_shed_subj_job)
        var itemRecyclerView: RecyclerView = itemView.findViewById(R.id.item_shed_subj_list_job)
        //val itemShedSubjJob: TextView = itemView.findViewById(R.id.item_shed_subj_job)
        private var adapter: SubjMondayHolder.ItemRecyclerAdapter? = ItemRecyclerAdapter(emptyList())

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

            val intent = Intent(activity,JobAddActivity::class.java)
            intent.putExtra(EXTRA_SUBJECT_NAME,subject.nameSubject)
            startActivity(intent)

        }

    }

    // адаптер для полного списка предметов
    private inner class SubjMondayAdapter (var subjects: List<Subject>): RecyclerView.Adapter<SubjMondayHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjMondayHolder {
            val view = layoutInflater.inflate(R.layout.list_item_subject_shedule,parent,false)
            return SubjMondayHolder(view)
        }

        override fun onBindViewHolder(holder: SubjMondayHolder, position: Int) {
            val subject = subjects[position]
            holder.bind(subject)
        }

        override fun getItemCount(): Int {
            return subjects.size
        }

    }

    private fun updateUI(subjects: List<Subject>){
        adapter = SubjMondayAdapter(subjects)
        subjMondayRecyclerView.adapter = adapter

    }


}