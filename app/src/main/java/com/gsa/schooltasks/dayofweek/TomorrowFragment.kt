package com.gsa.schooltasks.dayofweek

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gsa.schooltasks.JobViewModel
import com.gsa.schooltasks.R
import com.gsa.schooltasks.database.Job
import com.gsa.schooltasks.database.Subject
import java.text.SimpleDateFormat

private const val TAG = "TomorrowFragment"

class TomorrowFragment : Fragment() {

    var sdf: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy")

    private lateinit var subjTomorrowRecyclerView : RecyclerView
    private var adapter: TomorrowFragment.SubjTomorrowAdapter? = SubjTomorrowAdapter(emptyList())

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
        val view = inflater.inflate(R.layout.fragment_tomorrow, container, false)

        subjTomorrowRecyclerView = view.findViewById(R.id.subj_tomorrow_recycler_view) as RecyclerView
        subjTomorrowRecyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jobViewModel.getSubjectsForMonday().observe(viewLifecycleOwner, Observer {
                subjects -> subjects?.let {

            Log.i(TAG,"В пробном фрагменте ${subjects.size} предметов")
            updateUI(subjects)

        }
        })


    }

    // холдер для одного предмета в списке
    private inner class SubjTomorrowHolder (view: View) : RecyclerView.ViewHolder(view),View.OnClickListener {

        private lateinit var subject: Subject

        val itemShedSubjName: TextView = itemView.findViewById(R.id.item_shed_subj_name)
        var itemRecyclerView: RecyclerView = itemView.findViewById(R.id.item_shed_subj_list_job)
        //val itemShedSubjJob: TextView = itemView.findViewById(R.id.item_shed_subj_job)
        private var adapter: SubjTomorrowHolder.ItemRecyclerAdapter? = ItemRecyclerAdapter(emptyList())

        fun buildListJobs(nameSubject: String){
            jobViewModel.getNoSolvedJobsLiveData(nameSubject).observe(viewLifecycleOwner, Observer {
                jobs -> jobs?.let {
                    itemUpdateUI(jobs)
                Log.i(TAG,"В пробном фрагменте ${jobs.size} заданий")
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
                          //callbacks?.onJobSelected(job.idJob)
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

        fun bind(subject: Subject) {
            this.subject = subject
            itemShedSubjName.text = subject.nameSubject
            buildListJobs(subject.nameSubject)
            itemRecyclerView.layoutManager = LinearLayoutManager(context)
        }

        override fun onClick(v: View?) {

        }
    }

    // адаптер для полного списка предметов
    private inner class SubjTomorrowAdapter (var subjects: List<Subject>): RecyclerView.Adapter<SubjTomorrowHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjTomorrowHolder {
            val view = layoutInflater.inflate(R.layout.list_item_subject_shedule,parent,false)
            return SubjTomorrowHolder(view)
        }

        override fun onBindViewHolder(holder: SubjTomorrowHolder, position: Int) {
            val subject = subjects[position]
            holder.bind(subject)
        }

        override fun getItemCount(): Int {
            return subjects.size
        }

    }

    private fun updateUI(subjects: List<Subject>){
        adapter = SubjTomorrowAdapter(subjects)
        subjTomorrowRecyclerView.adapter = adapter

    }




}