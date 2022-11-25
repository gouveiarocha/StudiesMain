package com.gouveia.studiesmain.pps.kotlin

// INTRODUÇÃO INJEÇÃO DE DEPENDENCIA
// Ref. https://developer.android.com/training/dependency-injection?hl=pt-br#kotlin

private fun main() {

    // Sem injeção de dep.
    val car1 = Car1()
    car1.start()

    // C injeção de dep. via construtor.
    val engine = Engine()
    val car2 = Car2(engine)
    car2.start()

    // C injeção de dep. via campo.
    val car3 = Car3()
    car3.engine = Engine()
    car3.start()

    // C injeção de dep. via provider.
    val car4 = Car4()
    car4.start()

}

// Alto acoplamento. Aqui, a instancia de Engine é criada dentro da classe.
private class Car1 {
    private val engine = Engine()
    fun start() {
        engine.start()
    }
}

// Agora com injeção de depedencia pelo construtor, o modelo mais correto a ser usado. Essa
// abordagem permite passar diferentes modelos de Engine para o Car e facilita o teste.
private class Car2(private val engine: Engine) {
    fun start() {
        engine.start()
    }
}

// Agora com injeção de depedencia de campo. Devido algumas classes serem gerenciadas pelo Android,
// como activitys e views, uma alternatica é usar de campo, nesse modelo, elas são instanciadas
// apos a criação da classe.
private class Car3 {
    lateinit var engine: Engine
    fun start() {
        engine.start()
    }
}

// Agora, uma alternatica a injeção de dependencia, é usar um localizador de serviços, ou Provider,
// como nomeei aqui - observe que ele instancia um objeto que devolve a instancia de Engine.
private class Car4 {
    private val engine = EngineProvider.getEngine()
    fun start() {
        engine.start()
    }
}

// Exemplo usando injeção de dependencia com Koin.
class Car5(private val engine: Engine) {
    fun start() {
        engine.start()
    }
}

object EngineProvider {
    fun getEngine(): Engine = Engine()
}

class Engine {
    fun start() {
        println("Motor ligando... ")
    }
}