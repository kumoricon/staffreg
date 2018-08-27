Data Transfer Objects
=====================

This module contains request/response objects shared between other modules.
Generally data will be serialized to JSON and transferred between client and server
via REST.


Request/Response is from the client -> server perspective. For example,
HeartbeatRequest is a message from the client to the server. 
PasswordChangeResponse is a message that the server returns to the client.
