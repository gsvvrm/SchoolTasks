package com.gsa.schooltasks

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.gsa.schooltasks.database.Job
import java.text.SimpleDateFormat

class DialogSolvedJob (job: Job) : DialogFragment() {

    val TAG: String = DialogSolvedJob::class.java.simpleName

    var sdf: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy")

    var job = job

    private lateinit var jobSubjectDialogField: TextView
    private lateinit var jobContentDialogField: TextView
    private lateinit var jobDateDialogField: TextView
    private lateinit var jobSolvedButtonYes: Button
    private lateinit var jobSolvedButtonNo: Button

    val jobViewModel: JobViewModel by lazy {
        ViewModelProvider(this, defaultViewModelProviderFactory).get(JobViewModel::class.java)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
        val bundle = arguments

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dialog_solved, container, false)

        jobSubjectDialogField = view.findViewById(R.id.dialog_job_subject) as TextView

        jobContentDialogField = view.findViewById(R.id.dialog_job_content) as TextView

        jobDateDialogField = view.findViewById(R.id.dialog_job_date) as TextView

        jobSolvedButtonNo = view.findViewById(R.id.dialog_btn_no) as Button

        jobSolvedButtonYes = view.findViewById(R.id.dialog_btn_yes) as Button


        jobSubjectDialogField.text = job.nameSubjectJob
        jobContentDialogField.text = job.contentJob
        //jobDateField.text = job.dateJob.toString()
        jobDateDialogField.text = sdf.format(job.dateJob)

        jobSolvedButtonNo.setOnClickListener {
            closeDialog()
        }

        jobSolvedButtonYes.setOnClickListener {
            jobSolved(job)
            closeDialog()
        }


        return view
    }

    fun closeDialog(){
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }

    fun jobSolved (job: Job) {
        val job = job
        job.isSolvedJob = true
        jobViewModel.updateJob(job)
    }
}