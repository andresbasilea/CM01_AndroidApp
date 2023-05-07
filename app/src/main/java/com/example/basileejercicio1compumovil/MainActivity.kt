package com.example.basileejercicio1compumovil

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.basileejercicio1compumovil.databinding.ActivityMainBinding
import android.app.Dialog
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding


    var year = 0
    var month = 0
    var day = 0
    var numCuenta = 0


    private fun checkMail(mail: CharSequence) = (!TextUtils.isEmpty(mail) && Patterns.EMAIL_ADDRESS.matcher(mail).matches())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        ArrayAdapter.createFromResource(
            this,
            R.array.ListaCarreras,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        }

        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("LOGTAG", "################## El item seleccionado tiene la posición $position")
            }
            override fun onNothingSelected(parent: AdapterView<*>){

            }
        }
//        val intent = Intent(this, FormShow::class.java)
//        startActivity(intent)

    }

    // con algo de ayuda de chat gpt
    private fun validate(): Boolean {
        return when {
            binding.etNombre.text.isEmpty() -> {
                binding.etNombre.error = resources.getString(R.string.ValorR)
                Toast.makeText(this, resources.getString(R.string.NombreValidar), Toast.LENGTH_LONG).show()
                false
            }
            binding.etApellidos.text.isEmpty() -> {
                binding.etApellidos.error = resources.getString(R.string.ValorR)
                Toast.makeText(this, resources.getString(R.string.ApellidosValidar), Toast.LENGTH_LONG).show()
                false
            }

            binding.etNumCuenta.text.isEmpty() || binding.etNumCuenta.text.length != 9 -> {
                binding.etNumCuenta.error = resources.getString(R.string.ValorR)
                Toast.makeText(this, resources.getString(R.string.NumCuentaValidar), Toast.LENGTH_LONG).show()
                false
            }

            binding.etDate.text.isEmpty() -> {
                binding.etDate.error = resources.getString(R.string.ValorR)
                Toast.makeText(this, resources.getString(R.string.FechaNacimientoValidar), Toast.LENGTH_LONG).show()
                false
            }
            //Chat GPT
            !checkMail(binding.etCorreo.text) -> {
                binding.etCorreo.error = resources.getString(R.string.CorreoValidar)
                Toast.makeText(this, resources.getString(R.string.CorreoValidar), Toast.LENGTH_LONG).show()
                false
            }

            binding.spinner.selectedItemPosition == 0 -> {
                Toast.makeText(this, resources.getString(R.string.CarreraValidar), Toast.LENGTH_LONG).show()
                false
            }
            else -> true
        }
    }




    // Chat GPT
    fun calcularSignoZodiacal(fecha: String): String? {
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaNacimiento = formatoFecha.parse(fecha)
        val calendar = Calendar.getInstance().apply {
            time = fechaNacimiento!!
        }
        val dia = calendar.get(Calendar.DAY_OF_MONTH)
        val mes = calendar.get(Calendar.MONTH)
        val signos = resources.getStringArray(R.array.SignoZodiacal)
        val diasInicioSigno = intArrayOf(20, 19, 21, 20, 21, 21, 22, 23, 23, 23, 22, 22)
        var indiceSigno = mes
        if (dia < diasInicioSigno[mes]) {
            indiceSigno--
            if (indiceSigno < 0) {
                indiceSigno = 11
            }
        }
        return signos[indiceSigno]
    }
//
    //chat GPT
    fun calcularSignoZodiacalChino(fecha: String): String? {
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaNacimiento = formatoFecha.parse(fecha)
        val calendar = Calendar.getInstance()
        calendar.time = fechaNacimiento!!
        val year = calendar.get(Calendar.YEAR)
        val signos = resources.getStringArray(R.array.SignoChino)
        val cicloZodiacoChino = (year - 1900) % 12
        return signos[cicloZodiacoChino]
    }

    fun Calendario_click(view: View) {
        val Dialogfecha = DateSelect{year, month, day -> printDate(year, month, day)}

        Dialogfecha.show(supportFragmentManager, "DatePicker")
    }

    private fun printDate(year: Int, month: Int, day: Int){
        binding.etDate.setText("$day/$month/$year")
    }


    class DateSelect(val listener:(year:Int, month:Int, day:Int) -> Unit): DialogFragment(), DatePickerDialog.OnDateSetListener{

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog{
            val c = Calendar.getInstance()
            var year = c.get(Calendar.YEAR)
            var month = c.get(Calendar.MONTH)
            var day = c.get(Calendar.DAY_OF_MONTH)
            //var dayOfYear = c.get(Calendar.DAY_OF_YEAR)

            //val datePickerDialog = DatePickerDialog(requireActivity(),R.style.datePickerTheme,this, year, month, day)
            val datePickerDialog = DatePickerDialog(requireActivity(),this, year, month, day)
            val calendarMaxDate = Calendar.getInstance().apply {
                set(2010, 0, 1)}
            val maxDate = calendarMaxDate.timeInMillis
            //datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.datePicker.maxDate = maxDate
            val calendarMinDate = Calendar.getInstance().apply {
                set(1900, 0, 1)}
            val minDate = calendarMinDate.timeInMillis
            datePickerDialog.datePicker.minDate = minDate

            return datePickerDialog

        }

        override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
            listener(year,month+1,day)
        }
    }




//    //ChatGPT
    private fun obtenerEdad(FechaNacimiento: String): Int {
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaNac: Date = formatoFecha.parse(FechaNacimiento)
        val calendarFechaNac = Calendar.getInstance().apply {
            time = fechaNac
        }
        val calendarHoy = Calendar.getInstance()
        var edad = calendarHoy.get(Calendar.YEAR) - calendarFechaNac.get(Calendar.YEAR)
        if (calendarHoy.get(Calendar.DAY_OF_YEAR) < calendarFechaNac.get(Calendar.DAY_OF_YEAR)) {
            edad--
        }
        return edad
    }

    fun Continuar_click(view: View) {

        if (validate()) {
            Toast.makeText(this, resources.getString(R.string.IngresoCorrecto), Toast.LENGTH_LONG)
                .show()
            val intent = Intent(this, FormShow::class.java)

            val bundle = Bundle()
            val student1 = Student(binding.etNombre.text.toString(),binding.etApellidos.text.toString(),binding.etNumCuenta.text.toString().toInt(),obtenerEdad(binding.etDate.text.toString()),calcularSignoZodiacal(binding.etDate.text.toString()),calcularSignoZodiacalChino(binding.etDate.text.toString()),binding.etCorreo.text.toString(),binding.spinner.selectedItemPosition.toString().toInt())

            bundle.putParcelable("student1", student1)
            intent.putExtras(bundle)
            startActivity(intent)

        } else {
            Toast.makeText(this, resources.getString(R.string.IngresoIncorrecto), Toast.LENGTH_LONG)
                .show()
        }
    }




}