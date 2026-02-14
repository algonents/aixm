package com.algonents.aixm

import org.xml.sax.Attributes
import org.xml.sax.XMLReader
import org.xml.sax.helpers.XMLFilterImpl

class AixmNamespaceFilter(parent: XMLReader) : XMLFilterImpl(parent) {
    companion object {
        private const val AIXM_511 = "http://www.aixm.aero/schema/5.1.1"
        private const val AIXM_511_MSG = "http://www.aixm.aero/schema/5.1.1/message"
        private const val AIXM_51 = "http://www.aixm.aero/schema/5.1"
        private const val AIXM_51_MSG = "http://www.aixm.aero/schema/5.1/message"
    }

    private fun mapNs(uri: String): String = when (uri) {
        AIXM_51 -> AIXM_511
        AIXM_51_MSG -> AIXM_511_MSG
        else -> uri
    }

    override fun startElement(uri: String, localName: String, qName: String, atts: Attributes) {
        super.startElement(mapNs(uri), localName, qName, atts)
    }

    override fun endElement(uri: String, localName: String, qName: String) {
        super.endElement(mapNs(uri), localName, qName)
    }

    override fun startPrefixMapping(prefix: String, uri: String) {
        super.startPrefixMapping(prefix, mapNs(uri))
    }
}
