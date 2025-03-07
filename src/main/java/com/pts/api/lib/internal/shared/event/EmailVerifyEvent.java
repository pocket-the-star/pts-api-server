package com.pts.api.lib.internal.shared.event;

public record EmailVerifyEvent(String email, String authCode) {

}
