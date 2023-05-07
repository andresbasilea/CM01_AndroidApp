package com.example.basileejercicio1compumovil

import android.os.Parcelable;
import kotlinx.parcelize.Parcelize;

@Parcelize
class Student(
    var nombre: String?,
    var apellidos: String?,
    var numCuenta: Int,
    var edad: Int,
    var signo: String?,
    var signoChino: String?,
    var correo: String?,
    var carrera: Int
) : Parcelable