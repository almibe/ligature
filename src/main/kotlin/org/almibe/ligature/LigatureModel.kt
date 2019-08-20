/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.almibe.ligature

import java.io.Closeable
import java.math.BigDecimal
import java.util.*
import java.util.stream.Stream

sealed class Value
data class Node(val id: String) : Value()
data class Attribute(val attribute: String)
sealed class Literal: Value()

data class LangLiteral(val value: String, val langTag: String) : Literal()
data class StringLiteral(val value: String) : Literal()
data class BooleanLiteral(val value: Boolean): Literal()
data class LongLiteral(val value: Long) : Literal()
data class DecimalLiteral(val value: BigDecimal) : Literal()

data class Statement(val entity: Node,
                     val attribute: Attribute,
                     val value: Value,
                     val context: Node)

interface Store: Closeable {
    fun getDatasetNames(): Stream<String>
    fun getDataset(name: String): Dataset
    fun deleteDataset(name: String)
    override fun close()
}

interface Dataset {
    fun getDatasetName(): String
    fun addStatements(statements: Collection<Statement>)
    fun removeStatements(statements: Collection<Statement>)
    fun allStatements(): Stream<Statement>
    fun newNode(): Node
}
