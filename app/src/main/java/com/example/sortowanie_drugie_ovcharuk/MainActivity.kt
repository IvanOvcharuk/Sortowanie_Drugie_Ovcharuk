package com.example.sortowanie_drugie_ovcharuk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var brute: TextView
    private lateinit var KMP: TextView
    private lateinit var BM: TextView
    private lateinit var RK: TextView
    private lateinit var wykonaj: Button
    private lateinit var ilerazy: EditText
    private lateinit var ileelementow: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        brute = findViewById(R.id.brute)
        KMP = findViewById(R.id.KMP)
        BM = findViewById(R.id.BM)
        RK = findViewById(R.id.RK)
        wykonaj = findViewById(R.id.wykonaj)
        ilerazy = findViewById(R.id.ilerazy)
        ileelementow = findViewById(R.id.ileelementow)

        fun losowanie(size: Int): MutableList<Int> {
            val random = Random()
            return List(size) { random.nextInt(1000) }.toMutableList()
        }

        fun Czas(t1 : Long, t2 : Long) : Long
        {
            return t2 - t1
        }
        //brute force
        fun bruteForceSort(arr: IntArray): IntArray {
            val n = arr.size
            var permutations = 1
            for (i in 2..n) {
                permutations *= i
            }
            for (i in 0 until permutations) {
                for (j in 0 until n - 1) {
                    if (arr[j] > arr[j + 1]) {
                        val temp = arr[j]
                        arr[j] = arr[j + 1]
                        arr[j + 1] = temp
                    }
                }
            }
            return arr
        }

        //KMP
        fun kmpSort(arr: IntArray) {
            val n = arr.size
            val aux = IntArray(n)
            var lo = 0
            var hi = n - 1
            var d = 0
            var m = 1

            // Compute the number of digits in the largest number
            while (hi >= m) {
                m *= 10
                d++
            }

            // Perform d iterations of counting sort
            for (i in 1..d) {
                val count = IntArray(10)

                // Count the frequency of each digit
                for (j in lo..hi) {
                    val digit = (arr[j] / m) % 10
                    count[digit]++
                }

                // Compute the cumulative frequency
                for (j in 1 until 10) {
                    count[j] += count[j - 1]
                }

                // Copy the elements into the auxiliary array
                for (j in hi downTo lo) {
                    val digit = (arr[j] / m) % 10
                    aux[count[digit] - 1] = arr[j]
                    count[digit]--
                }

                // Copy the elements back into the original array
                for (j in lo..hi) {
                    arr[j] = aux[j - lo]
                }

                // Move to the next digit
                m /= 10
            }
        }
        //B-M
        fun boyerMooreSort(array: IntArray) {
            val n = array.size
            val maxVal = array.maxOrNull() ?: 0
            val buckets = IntArray(maxVal + 1)

            // Fill the buckets with the frequency of each value
            for (i in 0 until n) {
                buckets[array[i]]++
            }

            // Update the values in the array based on the bucket frequency
            var i = 0
            for (j in 0..maxVal) {
                repeat(buckets[j]) {
                    array[i++] = j
                }
            }
        }

        //R-B

        fun rabinKarpSort(arr: IntArray) {
            val n = arr.size
            if (n <= 1) return

            val bucketSize = 10 // rozmiar kubełka
            val buckets = Array(bucketSize) { mutableListOf<Int>() }

            // wyznaczenie wartości maksymalnej i obliczenie liczby cyfr w każdej liczbie
            var maxVal = arr[0]
            for (i in 1 until n) {
                if (arr[i] > maxVal) {
                    maxVal = arr[i]
                }
            }
            val numDigits = (Math.log10(maxVal.toDouble()) + 1).toInt()

            // sortowanie kubełkowe z wykorzystaniem hashowania
            var exp = 1
            for (i in 0 until numDigits) {
                for (j in 0 until n) {
                    val index = (arr[j] / exp) % bucketSize
                    buckets[index].add(arr[j])
                }
                var pos = 0
                for (j in 0 until bucketSize) {
                    for (k in 0 until buckets[j].size) {
                        arr[pos++] = buckets[j][k]
                    }
                    buckets[j].clear()
                }
                exp *= bucketSize
            }
        }



        wykonaj.setOnClickListener {
            if (ilerazy.text.isNotEmpty() && ileelementow.text.isNotEmpty())
            {
                val losowa_lista = losowanie(ileelementow.text.toString().toInt())

                var temp1 : Long; var temp2 : Long

                temp1 = System.currentTimeMillis()
                for (i in 0..ilerazy.text.toString().toInt()) {
                    val sortedArray = bruteForceSort(losowa_lista.toIntArray())
                    losowa_lista.clear()
                    losowa_lista.addAll(sortedArray.toList())
                }
                temp2 = System.currentTimeMillis()
                brute.text =Czas(temp1, temp2).toString() + " milisekund"

                temp1 = System.currentTimeMillis()
                for (i in 0..ilerazy.text.toString().toInt())
                    kmpSort(losowa_lista.toIntArray())
                temp2 = System.currentTimeMillis()
                KMP.text =Czas(temp1, temp2).toString() + " milisekund"

                temp1 = System.currentTimeMillis()
                for (i in 0..ilerazy.text.toString().toInt())
                    boyerMooreSort(losowa_lista.toIntArray())
                temp2 = System.currentTimeMillis()
                BM.text = Czas(temp1, temp2).toString() + " milisekund"

                temp1 = System.currentTimeMillis()
                for (i in 0..ilerazy.text.toString().toInt())
                    rabinKarpSort(losowa_lista.toIntArray())
                temp2 = System.currentTimeMillis()
                RK.text =Czas(temp1, temp2).toString() + " milisekund"

            }
            else
                Toast.makeText(this, "Pola nie moga byc puste!", Toast.LENGTH_SHORT).show()
        }
    }
}