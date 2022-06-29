package com.gsa.schooltasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gsa.schooltasks.database.Job
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "JobFragment"

private const val ARG_JOB_ID = "job_id"


// фрагмент для отображения задания
class JobFragment: Fragment() {

    var sdf: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy")

    private lateinit var job: Job
    private lateinit var jobSubjectField: TextView
    private lateinit var jobContentField: TextView
    private lateinit var jobDateField: TextView
    private lateinit var jobSolvedField: CheckBox

    val jobViewModel: JobViewModel by lazy {
        ViewModelProvider(this, defaultViewModelProviderFactory).get(JobViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        val jobId: UUID = arguments?.getSerializable(ARG_JOB_ID) as UUID
        jobViewModel.loadJob(jobId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_job,container, false)

        jobSubjectField = view.findViewById(R.id.res_job_subject) as TextView

        jobContentField = view.findViewById(R.id.res_job_content) as TextView

        jobDateField = view.findViewById(R.id.res_job_date) as TextView

        jobSolvedField = view.findViewById(R.id.res_job_solved) as CheckBox


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jobViewModel.jobLiveData.observe (
            viewLifecycleOwner, androidx.lifecycle.Observer {
                job -> job?.let {
                    this.job = job
                updateUI()
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()

        jobSolvedField.apply {
            setOnCheckedChangeListener { _, isChecked -> job.isSolvedJob = isChecked}
        }
    }

    override fun onStop() {
        super.onStop()
        jobViewModel.updateJob(job)
    }

    private fun updateUI() {
        jobSubjectField.text = job.nameSubjectJob
        jobContentField.text = job.contentJob
        //jobDateField.text = job.dateJob.toString()
        jobDateField.text = sdf.format(job.dateJob)
        jobSolvedField.isChecked = job.isSolvedJob

    }

    companion object {
        fun newInstance(jobId: UUID): JobFragment {
            val args = Bundle().apply {
                putSerializable(ARG_JOB_ID, jobId)
            }
            return JobFragment().apply {
                arguments = args
            }
        }
    }

}