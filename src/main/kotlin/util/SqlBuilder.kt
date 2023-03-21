package util

import org.intellij.lang.annotations.Language

class SqlBuilder {
    private val lines = mutableListOf<String>()

    fun append(@Language("SQL") line: String) = lines.add(line)

    fun result() = lines.fold("") { res, line ->
        val postfix = when {
            line.endsWith(";")
                    || line.startsWith("--")
                    || line.startsWith("#") -> "\n\n"

            else -> "\n\t"
        }
        res + line + postfix
    }.trim()
}

fun buildSql(builder: SqlBuilder.() -> Unit): String {
    return SqlBuilder().apply(builder).result()
}