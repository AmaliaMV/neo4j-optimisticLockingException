package com.example

import grails.gorm.transactions.Transactional

@Transactional
class ModelAService {

    ModelA get(Serializable id) {
        ModelA.get(id)
    }

    List<ModelA> list(Map args) {
        ModelA.list(args)
    }

    Long count() {
        ModelA.count()
    }

    void delete(Serializable id) {
        ModelA.get(id).delete()
    }

    ModelA save(ModelA modelA) {
        if (modelA.validate()) {
            modelA.save(validate: false)
        }

        return modelA
    }

}