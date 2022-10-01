package de.test.core;

import java.util.LinkedHashSet;
import java.util.Set;

public final class Agenda {

    private final Set<Talk> talks = new LinkedHashSet<>();

    void addTalk(Talk talk) {
        talks.add(talk);
    }

    Set<Talk> getTalks() {
        return talks;
    }

}
