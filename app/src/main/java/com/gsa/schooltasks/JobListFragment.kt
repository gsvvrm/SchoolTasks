package com.gsa.schooltasks

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.gsa.schooltasks.database.Job
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "JobListFragment"

// фрагмент списка заданий
class JobListFragment: Fragment() {


    var sdf: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy")

    interface Callbacks {
        fun onJobSelected (jobId: UUID)
    }

    private var callbacks: Callbacks? = null
    private lateinit var jobRecyclerView : RecyclerView
    private var adapter: JobAdapter? = JobAdapter(emptyList())

    val jobViewModel: JobViewModel by lazy {
        ViewModelProvider(this, defaultViewModelProviderFactory).get(JobViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_job_list,container,false)

        jobRecyclerView = view.findViewById(R.id.job_recycler_view) as RecyclerView
        jobRecyclerView.layoutManager = LinearLayoutManager(context)

        jobRecyclerView.adapter=adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jobViewModel.jobListLiveData.observe(
            viewLifecycleOwner,
            Observer { jobs ->
                jobs?.let {
                    Log.i(TAG,"Got jobs ${jobs.size}")


                    updateUI(jobs)
                }
            }
        )
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    // холдер для одного задания в списке
    private inner class JobHolder (view: View): RecyclerView.ViewHolder(view),View.OnClickListener{

        private lateinit var job: Job

        val itemJobSubject: TextView = itemView.findViewById(R.id.item_job_subject)
        val itemJobDate: TextView = itemView.findViewById(R.id.item_job_date)
        val itemJobContent: TextView = itemView.findViewById(R.id.item_job_content)

        val cardForJob : MaterialCardView = itemView.findViewById(R.id.card_for_job)


        init {
            itemView.setOnClickListener(this)
        }

        fun bind(job: Job){
            this.job = job
            itemJobSubject.text = job.nameSubjectJob
            //itemJobDate.text = job.dateJob.toString()
            itemJobDate.text = sdf.format(job.dateJob)
            itemJobContent.text = job.contentJob

            if (job.isSolvedJob) {
                cardForJob.setBackgroundColor(resources.getColor(R.color.colorNormal))
            } else {
                cardForJob.setBackgroundColor(resources.getColor(R.color.colorAccent))
            }
        }

        override fun onClick(v: View?) {
            callbacks?.onJobSelected(job.idJob)
        }
    }

    // адаптер для полного списка заданий
    private inner class JobAdapter (var jobs: List<Job>): RecyclerView.Adapter<JobHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobHolder {
            val view = layoutInflater.inflate(R.layout.list_item_job,parent,false)
            return JobHolder(view)
        }

        override fun onBindViewHolder(holder: JobHolder, position: Int) {
            val job = jobs[position]
            holder.bind(job)
        }

        override fun getItemCount(): Int {
            return jobs.size
        }
    }

    private fun updateUI(jobs: List<Job>){
        adapter = JobAdapter(jobs)
        jobRecyclerView.adapter = adapter
    }

    companion object {
        fun newInstance (): JobListFragment {
            return JobListFragment()
        }
    }
}