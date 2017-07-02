/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.almibe.ligature.parsers

import org.almibe.ligature.*
import org.almibe.ligature.parser.NTriplesParser
import org.almibe.ligature.parser.TurtleLexer
import org.almibe.ligature.parser.TurtleListener
import org.almibe.ligature.parser.TurtleParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.tree.TerminalNode

class Turtle {
    fun parseTurtle(text: String) : List<Triple> {
        val parser = TurtleParserInstance()
        return parser.parseTurtle(text)
    }
}

private class TurtleParserInstance {
    fun parseTurtle(text: String): List<Triple> {
        val stream = CharStreams.fromString(text)
        val lexer = TurtleLexer(stream)
        val tokens = CommonTokenStream(lexer)
        val parser = TurtleParser(tokens)

        val walker = ParseTreeWalker()
        val listener = TriplesTurtleListener()
        walker.walk(listener, parser.turtleDoc())
        return listener.triples
    }
}

private class TurtleStatement {
    val subjects = mutableListOf<Subject>()
    val blankNodePropertyList = mutableListOf<Pair<IRI, MutableList<Object>>>()
    val predicateObjectList = mutableListOf<Pair<IRI, MutableList<Object>>>()

    fun computeTriples(): List<Triple> {
        if (subjects.size == 1) {
            return predicateObjectList.map { Triple(subjects.first(), it.first, it.second.first()) }
        } else {
            TODO("finish")
        }
    }
}

private class TriplesTurtleListener : TurtleListener {
    val triples = mutableListOf<Triple>()
    val prefixes: MutableMap<String, String> = mutableMapOf()
    lateinit var base: String
    var currentStatement: TurtleStatement = TurtleStatement()

    override fun exitSubject(ctx: TurtleParser.SubjectContext) {
        //TODO handle all subject logic here
        if (ctx.iri() != null) {
            currentStatement.subjects.add(handleIRI(ctx.iri().text))
        } else if (ctx.collection() != null) {

        } else if (ctx.blankNode() != null) {

        } else {
            throw RuntimeException("Unexpected subject.")
        }
    }

    override fun exitVerbObjectList(ctx: TurtleParser.VerbObjectListContext) {
        //TODO add verb object list pair to currentStatement
        val iri = handleIRI(ctx.verb().text)
        val `object`: Object = handleObject(ctx.objectList().`object`().first())
        currentStatement.predicateObjectList.add(Pair(iri, mutableListOf(`object`)))
    }

    override fun exitBlankNodePropertyList(ctx: TurtleParser.BlankNodePropertyListContext) {
        //TODO handle all blankNodePropertyList logic here

    }

    override fun exitTriples(ctx: TurtleParser.TriplesContext) {
        triples.addAll(currentStatement.computeTriples())
    }

    override fun exitBlankNode(ctx: TurtleParser.BlankNodeContext) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitLiteral(ctx: TurtleParser.LiteralContext) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitCollection(ctx: TurtleParser.CollectionContext) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitSparqlBase(ctx: TurtleParser.SparqlBaseContext) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitPrefixID(ctx: TurtleParser.PrefixIDContext) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitNumericLiteral(ctx: TurtleParser.NumericLiteralContext) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitSparqlPrefix(ctx: TurtleParser.SparqlPrefixContext) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitBase(ctx: TurtleParser.BaseContext) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitErrorNode(node: ErrorNode?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitVerb(ctx: TurtleParser.VerbContext) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitBooleanLiteral(ctx: TurtleParser.BooleanLiteralContext) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitObject(ctx: TurtleParser.ObjectContext) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitEveryRule(ctx: ParserRuleContext) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitTurtleDoc(ctx: TurtleParser.TurtleDocContext) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitRdfLiteral(ctx: TurtleParser.RdfLiteralContext) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitString(ctx: TurtleParser.StringContext) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun enterTriples(ctx: TurtleParser.TriplesContext) {
        currentStatement = TurtleStatement()
    }

    override fun exitDirective(ctx: TurtleParser.DirectiveContext) {
        if (ctx.base() != null) {
            this.base = ctx.base().IRIREF().text
        } else if (ctx.prefixID() != null) {
            this.prefixes[ctx.prefixID().PNAME_NS().text] = ctx.prefixID().IRIREF().text
        } else if (ctx.sparqlBase() != null) {
            this.base = ctx.sparqlBase().IRIREF().text
        } else if (ctx.sparqlPrefix() != null) {
            this.prefixes[ctx.sparqlPrefix().PNAME_NS().text] = ctx.sparqlPrefix().IRIREF().text
        } else {
            throw RuntimeException("Unexpected directive type.")
        }
    }

    override fun exitObjectList(ctx: TurtleParser.ObjectListContext) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitPrefixedName(ctx: TurtleParser.PrefixedNameContext) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitTerminal(node: TerminalNode?) { /* do nothing */ }
    override fun enterString(ctx: TurtleParser.StringContext) { /* do nothing */ }
    override fun exitPredicate(ctx: TurtleParser.PredicateContext) { /* do nothing */ }
    override fun enterDirective(ctx: TurtleParser.DirectiveContext) { /* do nothing */ }
    override fun exitPredicateObjectList(ctx: TurtleParser.PredicateObjectListContext) { /* do nothing */ }
    override fun enterPredicate(ctx: TurtleParser.PredicateContext) { /* do nothing */ }
    override fun exitIri(ctx: TurtleParser.IriContext) { /* do nothing */ }
    override fun enterObjectList(ctx: TurtleParser.ObjectListContext) { /* do nothing */ }
    override fun enterVerbObjectList(ctx: TurtleParser.VerbObjectListContext) { /* do nothing */}
    override fun enterVerb(ctx: TurtleParser.VerbContext) { /* do nothing */ }
    override fun enterSparqlPrefix(ctx: TurtleParser.SparqlPrefixContext) { /* do nothing */ }
    override fun enterBlankNode(ctx: TurtleParser.BlankNodeContext) { /* do nothing */ }
    override fun enterBooleanLiteral(ctx: TurtleParser.BooleanLiteralContext) { /* do nothing */ }
    override fun enterBlankNodePropertyList(ctx: TurtleParser.BlankNodePropertyListContext) { /* do nothing */ }
    override fun enterRdfLiteral(ctx: TurtleParser.RdfLiteralContext) { /* do nothing */ }
    override fun enterObject(ctx: TurtleParser.ObjectContext) { /* do nothing */ }
    override fun enterCollection(ctx: TurtleParser.CollectionContext) { /* do nothing */ }
    override fun enterLiteral(ctx: TurtleParser.LiteralContext) { /* do nothing */ }
    override fun enterEveryRule(ctx: ParserRuleContext) { /* do nothing */ }
    override fun enterPredicateObjectList(ctx: TurtleParser.PredicateObjectListContext) { /* do nothing */ }
    override fun enterSparqlBase(ctx: TurtleParser.SparqlBaseContext) { /* do nothing */ }
    override fun enterPrefixedName(ctx: TurtleParser.PrefixedNameContext) { /* do nothing */ }
    override fun enterPrefixID(ctx: TurtleParser.PrefixIDContext) { /* do nothing */ }
    override fun enterBase(ctx: TurtleParser.BaseContext) { /* do nothing */ }
    override fun enterNumericLiteral(ctx: TurtleParser.NumericLiteralContext) { /* do nothing */ }
    override fun enterTurtleDoc(ctx: TurtleParser.TurtleDocContext) { /* do nothing */ }
    override fun enterIri(ctx: TurtleParser.IriContext) { /* do nothing */ }
    override fun enterSubject(ctx: TurtleParser.SubjectContext) { /* do nothing */ }
    override fun enterStatement(ctx: TurtleParser.StatementContext) { /* do nothing */ }
    override fun exitStatement(ctx: TurtleParser.StatementContext) { /* do nothing */ }
}

internal fun handleObject(ctx: TurtleParser.ObjectContext): Object {
    return when {
        ctx.literal() != null -> handleTurtleLiteral(ctx.literal())
        ctx.blankNode() != null -> handleBlankNode(ctx.blankNode().text)
        ctx.iri() != null -> handleIRI(ctx.iri().text)
        ctx.blankNodePropertyList() != null -> TODO()
        ctx.collection() != null -> TODO()
        else -> throw RuntimeException("Unexpected object")
    }
}

internal fun handleTurtleLiteral(ctx: TurtleParser.LiteralContext): Literal {
    return when {
        ctx.booleanLiteral() != null -> handleBooleanLiteral(ctx.booleanLiteral())
        ctx.numericLiteral() != null  -> handleNumericLiteral(ctx.numericLiteral())
        ctx.rdfLiteral() != null  -> handleRdfLiteral(ctx.rdfLiteral())
        else -> throw RuntimeException("Unexpected literal")
    }
}

fun  handleBooleanLiteral(ctx: TurtleParser.BooleanLiteralContext): Literal {
    TODO()
}

fun  handleNumericLiteral(ctx: TurtleParser.NumericLiteralContext): Literal {
    TODO()
}

internal fun handleRdfLiteral(ctx: TurtleParser.RdfLiteralContext): Literal {
    val value = if (ctx.string().text.length >= 2) {
        ctx.string().text.substring(1, ctx.string().text.length-1)
    } else {
        throw RuntimeException("Invalid literal.")
    }

    return when {
        ctx.LANGTAG() != null -> LangLiteral(value, ctx.LANGTAG().text.substring(1))
        ctx.iri() != null -> TypedLiteral(value, handleIRI(ctx.iri().text))
        else -> TypedLiteral(value)
    }
}