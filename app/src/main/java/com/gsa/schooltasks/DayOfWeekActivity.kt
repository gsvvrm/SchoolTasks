package com.gsa.schooltasks

import android.content.ComponentName
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.gsa.schooltasks.database.Subject
import com.gsa.schooltasks.dayofweek.*
import com.gsa.schooltasks.services.CheckNewJobService
import com.gsa.schooltasks.services.CheckNewJobWorker
import java.util.concurrent.TimeUnit
import android.content.pm.PackageManager

import android.content.pm.ResolveInfo
import androidx.lifecycle.ViewModelProvider
import java.lang.Exception


private const val WORK_CHECK_NEW_JOB = "CHECK_NEW_JOB"

// активность для отображения дней недели
class DayOfWeekActivity: AppCompatActivity() {

    // проверка на первый запуск приложения
    private lateinit var prefs: SharedPreferences

    private lateinit var viewPagerFragmentAdapterForDayOfWeek: ViewPagerFragmentAdapterForDayOfWeek
    private lateinit var dayOfWeekViewPager: ViewPager2
    private lateinit var tabLayoutOfDayOfWeek: TabLayout
    private lateinit var fabAddNewJob: ExtendedFloatingActionButton

    val jobViewModel: JobViewModel by lazy {
        ViewModelProvider(this, defaultViewModelProviderFactory).get(JobViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ativity_day_of_week)

        prefs = getSharedPreferences("com.gsa.schooltasks", MODE_PRIVATE);


        //кнопка добавления нового задания
        fabAddNewJob = findViewById(R.id.fab_add_job)
        fabAddNewJob.setOnClickListener {
            val intent = Intent(this, JobAddActivity::class.java)
            startActivity(intent)
        }

        //фрагменты дней недели
        viewPagerFragmentAdapterForDayOfWeek = ViewPagerFragmentAdapterForDayOfWeek(this)
        dayOfWeekViewPager = findViewById(R.id.day_of_week_view_pager)
        dayOfWeekViewPager.adapter = viewPagerFragmentAdapterForDayOfWeek

        //загаловки фрагментов дней недели
        tabLayoutOfDayOfWeek = findViewById(R.id.tab_layout_of_day_of_week)

        TabLayoutMediator(tabLayoutOfDayOfWeek, dayOfWeekViewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.setText(R.string.tomorrow)
                }
                1 -> {
                    tab.setText(R.string.sm_monday)
                }
                2 -> {
                    tab.setText(R.string.sm_tuesday)
                }
                3 -> {
                    tab.setText(R.string.sm_wednesday)
                }
                4 -> {
                    tab.setText(R.string.sm_thursday)
                }
                5 -> {
                    tab.setText(R.string.sm_friday)
                }
                6 -> {
                    tab.setText(R.string.sm_saturday)
                }
                7 -> {
                    tab.setText(R.string.sm_sunday)
                }
            }

        }.attach()


    }

    //создание меню выбора
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.activity_job, menu)
        val menuItem: MenuItem = menu?.findItem(R.id.menu_set_autostart) as MenuItem
        menuItem.isVisible = setHideItemMenu()
        return true

    }

    // обработка нажания элементов меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_list_subject -> {
                //откытие списка предметов
                val intentListSub = Intent(this, SubjectsListActivity::class.java)
                startActivity(intentListSub)
                true
            }
            R.id.menu_list_jobs_all -> {
                //открытие всех заданий
                val intentListJobs = Intent(this, JobMainActivity::class.java)
                startActivity(intentListJobs)
                true
            }
            R.id.menu_set_autostart -> {
                //открытие экрана включения автозагрузки приложения
                setAppAutoRun()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()



        // добавление предметов при первом запуске приложения
        if (prefs.getBoolean("firstrun", true)) {

            jobViewModel.addSubject(subject = Subject(0,"Математика",true,false,false,false,false,false,false))
            jobViewModel.addSubject(subject = Subject(0,"Литература",true,false,false,false,false,false,false))


            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }

    private fun setAppAutoRun() {

        val manufacturer = Build.MANUFACTURER
        try {
            val intent = Intent()
            if ("xiaomi".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName("com.miui.securitycenter",
                    "com.miui.permcenter.autostart.AutoStartManagementActivity")
            } else if ("oppo".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName("com.coloros.safecenter",
                    "com.coloros.safecenter.permission.startup.StartupAppListActivity")
            } else if ("vivo".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName("com.vivo.permissionmanager",
                    "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")
            } else if ("Letv".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName("com.letv.android.letvsafe",
                    "com.letv.android.letvsafe.AutobootManageActivity")
            } else if ("Honor".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName("com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.process.ProtectActivity")
            }
            val list =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            if (list.size > 0) {
                startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setHideItemMenu() : Boolean {
        val manufacturer = Build.MANUFACTURER

        when {
            "xiaomi".equals(manufacturer, ignoreCase = true) -> {
                return true
            }
            "oppo".equals(manufacturer, ignoreCase = true) -> {
                return true
            }
            "vivo".equals(manufacturer, ignoreCase = true) -> {
                return true
            }
            "Letv".equals(manufacturer, ignoreCase = true) -> {
                return true
            }
            "Honor".equals(manufacturer, ignoreCase = true) -> {
                return true
            }
        }

        return false
    }



}