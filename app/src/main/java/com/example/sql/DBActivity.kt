package com.example.sql

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sql.databinding.ActivityDbactivityBinding

class DBActivity : AppCompatActivity() {
    private val db = DBHelper(this, null)
    private lateinit var binding: ActivityDbactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDbactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            saveBTN.setOnClickListener {
                if (nameET.text.isBlank() || phoneET.text.isBlank()) {
                    Toast.makeText(this@DBActivity, R.string.fill_all_fields, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val person = Person(
                    nameET.text.toString(),
                    roleS.selectedItem as String,
                    phoneET.text.toString()
                )
                db.addPerson(person)
                nameET.text.clear()
                phoneET.text.clear()
                roleS.setSelection(0)
            }
            loadBTN.setOnClickListener {
                loadData()
            }
            deleteBTN.setOnClickListener {
                db.removeAll()
            }
            val roles = listOf("Engineer", "Mechanic", "Security", "Programmer")
            val adapter = ArrayAdapter(this@DBActivity, android.R.layout.simple_spinner_item, roles).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            roleS.adapter = adapter
            roleS.setSelection(0)
        }
    }

    private fun loadData() {
        binding.apply {
            namesTV.text = ""
            rolesTV.text = ""
            phonesTV.text = ""
            val cursor = db.getInfo()
            if (cursor.moveToFirst()) {
                do {
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_NAME))
                    val role = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_ROLE))
                    val phone = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_PHONE))
                    namesTV.append("$name\n")
                    rolesTV.append("$role\n")
                    phonesTV.append("$phone\n")
                } while (cursor.moveToNext())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_exit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_exit -> {
                moveTaskToBack(true)
                finish()
                return true
            }
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}