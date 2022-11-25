package com.gouveia.studiesmain.pps.kotlin.tests

fun main() {
    testaComandoAll()
    testaComandoAny()
}

private fun testaComandoAny() {

    // COMANDO ANY (COLECTIONS)

    val arrayOfBoolean = arrayOf(true, false, false)
    //ex ret. true se achar pelo menos um elemento true na lista.
    val test = arrayOfBoolean.any { it }
    println(test)

    val arrayOfString = arrayOf("Oie", "Tudo", "Bem")
    //ex ret. true se achar pelo menos um elemento testado na na lista.
    val test2 = arrayOfString.any { it == "Oie" }
    println(test2)

}
private fun testaComandoAll(){

    // COMANDO ALL (COLECTIONS)

    val arrayOfBoolean = arrayOf(true, true, true)
    val test = arrayOfBoolean.all { it } // ret. true se todos os elementos do array forem true
    println(test)

    val arrayOfString1 = arrayOf("Teste0, Teste2")
    val arrayOfString2 = arrayOf("Teste1, Teste2", "Teste3")
    // ret. true se todos os elementos da lista1 constarem na lista2
    val test2: Boolean = arrayOfString1.all { it in arrayOfString2 }
    println(test2)

}