package com.algonents.aixm

import jakarta.xml.bind.JAXBContext
import kotlin.test.Test
import kotlin.test.assertNotNull

class AixmParserTest {
    @Test
    fun `unmarshal sample airport XML`() {
        val input = javaClass.classLoader.getResourceAsStream("samples/Airport.xml")!!
        val context = JAXBContext.newInstance("com.algonents.aixm.message")
        val unmarshaller = context.createUnmarshaller()
        val result = unmarshaller.unmarshal(input)
        assertNotNull(result)
    }
}
