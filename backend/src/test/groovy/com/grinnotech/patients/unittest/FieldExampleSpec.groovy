package com.grinnotech.patients.unittest

import com.grinnotech.testSpock.service.MessageService
import org.junit.experimental.categories.Category
import spock.lang.Shared
import spock.lang.Specification

@Category(UnitTest.class)
class FieldExampleSpec extends Specification {

	def messageService = new MessageService()
	@Shared sharedObject = new Object()

	def 'Get message one'() {
		println 'First feature method'
		println 'unique object: ' + messageService
		println 'shared object: ' + sharedObject

		expect: 'Should return the correct message'
		messageService.getMessage() == 'Hello'
	}

	def 'Get message two'() {
		println 'Second feature method'
		println 'unique object: ' + messageService
		println 'shared object: ' + sharedObject

		expect: 'Should return the correct message'
		messageService.getMessage("World") == 'Hello, World!'
	}
}
