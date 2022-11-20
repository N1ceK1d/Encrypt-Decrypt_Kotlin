import java.io.File

fun main(args: Array<String>) {
    /**
    Параметры: -data -in -out -key -mode -alg

    +1. если нет -mode, то enc
    +2. если нет -key, то key = 0
    +3. если нет -data и нет -in, то пустая строка
    +4. если нет -out, то вывод в консоль
    +5. если есть -data и есть -in, то -in в приоритете
    +6. если нет -alg, то shift
    +7. если есть какие-либо проблемы, то программа должна вывести Error
     **/

    val message: String = if ("-in" in args) {
            File(args[args.indexOf("-in") + 1]).readText()
        } else if ("-data" in args && "-in" !in args) {
            args[args.indexOf("-data") + 1] } else { " " }
    val key: Int = if ("-key" in args) {args[args.indexOf("-key") + 1].toInt()} else {0}
    val mode: String = if ("-mode" in args) {args[args.indexOf("-mode") + 1]} else { "enc" }
    val alg: String = if ("-alg" in args) {args[args.indexOf("-alg") + 1]} else { "shift" }
    
    var out: String = ""

    when(mode) {
        "enc" -> when(alg) {
            "unicode" -> out = unicodeEnc(message, key)
            "shift" -> out = shiftEnc(message, key)
            else -> println("Error")
        }
        "dec" -> when(alg) {
            "unicode" -> out = unicodeDec(message, key)
            "shift" -> out = shiftDec(message, key)
            else -> println("Error")
        }
        else -> println("Error")
    }

    if("-out" in args) {
        File(args[args.indexOf("-out") + 1]).writeText(out)
    } else {
        println(out)
    }
}

//работает
fun shiftEnc(message: String, key: Int): String {
    val lowerAlphabet = "abcdefghijklmnopqrstuvwxyz"
    val upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    return message.map({if(it in lowerAlphabet) {
        lowerAlphabet[(lowerAlphabet.indexOf(it) + key) % lowerAlphabet.length]
    } else if (it !in lowerAlphabet && it !in upperAlphabet) {
        it
    }
    else {
        upperAlphabet[(upperAlphabet.indexOf(it) + key) % upperAlphabet.length]
    }}).joinToString("")
}

fun shiftDec(message: String, key: Int): String {
    return message.map(
        {
            if(it in 'A'..'Z')
            {
                var t1 = it-'A'- key;
                if( t1 < 0 ) {
                    t1 = 26 + t1;
                }
                (t1 + 'A'.code).toChar();
            }
            else if(it in 'a'..'z')
            {
                var t1=it-'a'- key;
                if(t1<0) {
                    t1 = 26 + t1;
                }
                (t1 + 'a'.code).toChar();
            } else {
                it
            }
        }).joinToString("")
}

//работает
fun unicodeEnc(message: String, key: Int): String {
    return message.map({it + key}).joinToString("")
}

fun unicodeDec(message: String, key: Int): String {
    return message.map({it - key}).joinToString("")
}
