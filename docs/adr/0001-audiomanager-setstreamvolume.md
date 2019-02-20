# 1. AudioManager setStreamVolume

Date: 2019-02-20

## Status

Accepted

## Context

The method adjustStreamVolume() used with AudioManager.ADJUST_TOGGLE_MUTE or AudioManager.ADJUST_MUTE and ADJUST_UNMUTE has a weird bug that will prevent the change if mute was activated via system UI. Although we toggled multiple times (and waited several seconds between), the mute state was not toggled. Also if we mute with the widget, we need two taps on the system UI to toggle the state. 

## Decision

Use setStreamVolume() instead. 

## Consequences

We need to store the last set volume to be able to restore it after mute.