print("Hello kotlin")



fun workTo(end: Int, action: (Int) -> Unit): Unit {
    (1..end).forEach { n -> action(n) }
}


workTo(10, {num -> println(num)})
//equal
workTo(10) { num -> println(num) }
//equal
workTo(10, ::println)


val names = listOf("rick", "david", "genie", "evan", "mido", "jk")

println(names.find { name: String -> name.length > 3 })


fun predicateOfList(size: Int) = {
   input: String -> input.length == size
}

println(names.find(predicateOfList(5)))

