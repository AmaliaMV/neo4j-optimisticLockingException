package com.example

import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback
import static org.springframework.http.HttpStatus.*
import geb.spock.*
import grails.plugins.rest.client.RestBuilder

@Integration
@Rollback
class ModelAFunctionalSpec extends GebSpec {

    RestBuilder getRestBuilder() {
        new RestBuilder()
    }

    String getResourcePath() {
        "${baseUrl}/modelA"
    }

    void "Test the update action correctly updates an instance"() {
        when:"The save action is executed with valid data"
        def response = restBuilder.post(resourcePath) {
            json([name: "model A test"])
        }        

        then:"The response is correct"
        response.status == CREATED.value()
        response.json.id != null

        when:"The update action is called with valid data"
        def id = response.json.id
        def version = response.json.version
        def data1 = [name: "model A updated 1", version: version]
        def data2 = [name: "model A updated 2", version: version]

        response = restBuilder.put("$resourcePath/$id") {
            json(data1)
        }

        then:"The response is correct"
        response.status == OK.value()
        response.json

        when:"The update action is called with an old version"
        response = restBuilder.put("$resourcePath/$id") {
            json(data2)
        }

        then:"The response is incorrect"
        response.status == INTERNAL_SERVER_ERROR.value()

        cleanup:
        ModelA.getAll()*.delete()
    }
}