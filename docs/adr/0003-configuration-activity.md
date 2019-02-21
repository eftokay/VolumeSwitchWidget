# 3. Configuration activity

Date: 2019-02-21

## Status

Accepted

Amends [2. Ringtone widget](0002-ringtone-widget.md)

## Context

Changing the ringtone settings requires further permissions which have to be granted by the user. This can not (easily/proper) be done within the widget itself.

## Decision

A configuration activity should be provided 
- to guide the user why the widget requires further permissions and
- to handle the whole permission request process 

## Consequences

- Implementation of the activity
- Starting the activity by adding the ringtone widget
- Request the extra permission