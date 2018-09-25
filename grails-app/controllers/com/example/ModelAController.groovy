package com.example

import grails.databinding.SimpleMapDataBindingSource
import grails.gorm.transactions.Transactional
import grails.validation.ValidationException
import grails.web.databinding.DataBindingUtils

import static org.springframework.http.HttpStatus.*

class ModelAController {

    ModelAService modelAService

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond modelAService.list(params), model:[modelACount: modelAService.count()]
    }

    def show(Long id) {
        respond modelAService.get(id)
    }

    def save(ModelA modelA) {
        if (modelA == null) {
            render status: NOT_FOUND
            return
        }

        try {
            modelAService.save(modelA)
        } catch (ValidationException e) {
            respond modelA.errors, view:'create'
            return
        }

        respond modelA, [status: CREATED, view:"show"]
    }

    @Transactional
    def update() {
        ModelA modelA = modelAService.get(params.id)

        if (modelA == null) {
            render status: NOT_FOUND
            return
        }

        try {
            DataBindingUtils.bindObjectToInstance(modelA, new SimpleMapDataBindingSource(request.JSON), null, ['dateCreated'], null)
            modelAService.save(modelA)

        } catch (ValidationException e) {
            respond modelA.errors, view:'edit'
            return
        }

        respond modelA, [status: OK, view:"show"]
    }

    def delete(Long id) {
        if (id == null) {
            render status: NOT_FOUND
            return
        }

        modelAService.delete(id)

        render status: NO_CONTENT
    }
}
