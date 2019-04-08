package gateway.model

import groovy.transform.Canonical

@Canonical
class Rute {
    String id
    List<String> predicates
    String uri
    String sikkerhed
}
