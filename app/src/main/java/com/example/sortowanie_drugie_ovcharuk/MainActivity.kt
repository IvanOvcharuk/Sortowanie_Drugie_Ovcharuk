package com.example.sortowanie_drugie_ovcharuk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var wstawianie: TextView
    private lateinit var babelkowe: TextView
    private lateinit var heapsort: TextView
    private lateinit var scalanie: TextView
    private lateinit var szybkie: TextView
    private lateinit var wykonaj: Button
    private lateinit var ilerazy: EditText
    private lateinit var ileelementow: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        wstawianie = findViewById(R.id.brute)
        babelkowe = findViewById(R.id.KMP)
        heapsort = findViewById(R.id.BM)
        scalanie = findViewById(R.id.RK)
        szybkie = findViewById(R.id.szybkie)
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
        //Szybkie
        fun partition(array: MutableList<Int>, low: Int, high: Int): Int {
            val pivot = array[high]
            var i = low - 1
            for (j in low until high) {
                if (array[j] <= pivot) {
                    i++
                    val temp = array[i]
                    array[i] = array[j]
                    array[j] = temp
                }
            }
            val temp = array[i + 1]
            array[i + 1] = array[high]
            array[high] = temp
            return i + 1
        }

        fun szybkie(array: MutableList<Int>, low: Int, high: Int) {
            if (low < high) {
                val pivot = partition(array, low, high)
                szybkie(array, low, pivot - 1)
                szybkie(array, pivot + 1, high)
            }
        }

        //Heapsort
        fun heapify(array: MutableList<Int>, n: Int, i: Int) {
            var largest = i
            val l = 2 * i + 1
            val r = 2 * i + 2

            if (l < n && array[l] > array[largest]) {
                largest = l
            }

            if (r < n && array[r] > array[largest]) {
                largest = r
            }

            if (largest != i) {
                val temp = array[i]
                array[i] = array[largest]
                array[largest] = temp

                heapify(array, n, largest)
            }
        }

        fun heapsort(array: MutableList<Int>) {
            for (i in array.size / 2 - 1 downTo 0) {
                heapify(array, array.size, i)
            }

            for (i in array.size - 1 downTo 0) {
                val temp = array[0]
                array[0] = array[i]
                array[i] = temp

                heapify(array, i, 0)
            }
        }

        //Scalanie
        fun merge(left: IntArray, right: IntArray): IntArray {
            var leftIndex = 0
            var rightIndex = 0
            val result = IntArray(left.size + right.size)

            for (i in result.indices) {
                if (leftIndex >= left.size) {
                    result[i] = right[rightIndex++]
                } else if (rightIndex >= right.size) {
                    result[i] = left[leftIndex++]
                } else if (left[leftIndex] < right[rightIndex]) {
                    result[i] = left[leftIndex++]
                } else {
                    result[i] = right[rightIndex++]
                }
            }

            return result
        }
        fun scalanie(array: IntArray): IntArray {
            if (array.size <= 1) return array

            val mid = array.size / 2
            val left = scalanie(array.copyOfRange(0, mid))
            val right = scalanie(array.copyOfRange(mid, array.size))

            return merge(left, right)
        }
        wykonaj.setOnClickListener {
            if (ilerazy.text.isNotEmpty() && ileelementow.text.isNotEmpty())
            {
                val losowa_lista = losowanie(ileelementow.text.toString().toInt())

                var temp1 : Long; var temp2 : Long

                temp1 = System.currentTimeMillis()
                for (i in 0..ilerazy.text.toString().toInt())
                    wstawianie(losowa_lista)
                temp2 = System.currentTimeMillis()
                wstawianie.text = Czas(temp1, temp2).toString() + " milisekund"

                temp1 = System.currentTimeMillis()
                for (i in 0..ilerazy.text.toString().toInt())
                    babelkowe(losowa_lista)
                temp2 = System.currentTimeMillis()
                babelkowe.text =Czas(temp1, temp2).toString() + " milisekund"

                temp1 = System.currentTimeMillis()
                for (i in 0..ilerazy.text.toString().toInt())
                    szybkie(losowa_lista, 0, losowa_lista.size - 1)
                temp2 = System.currentTimeMillis()
                szybkie.text = Czas(temp1, temp2).toString() + " milisekund"

                temp1 = System.currentTimeMillis()
                for (i in 0..ilerazy.text.toString().toInt())
                    heapsort(losowa_lista)
                temp2 = System.currentTimeMillis()
                heapsort.text =Czas(temp1, temp2).toString() + " milisekund"

                temp1 = System.currentTimeMillis()
                for (i in 0..ilerazy.text.toString().toInt())
                    scalanie(losowa_lista.toIntArray())
                temp2 = System.currentTimeMillis()
                scalanie.text = Czas(temp1, temp2).toString() + " milisekund"
            }
            else
                Toast.makeText(this, "Pola nie moga byc puste!", Toast.LENGTH_SHORT).show()
        }
    }
}