package com.theapache64.phokuzed.ui.screen.blocklist

// Modes supported by the Blocklist screen
enum class Mode {
    /**
     * To add domains to the list.
     * This will also update the /etc/hosts file.
     */
    ADD,

    /**
     * To add and delete domains to/from the list.
     */
    ADD_AND_REMOVE, // Add and delete domains
}
