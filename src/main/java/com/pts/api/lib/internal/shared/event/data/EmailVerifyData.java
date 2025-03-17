package com.pts.api.lib.internal.shared.event.data;

import com.pts.api.lib.internal.shared.event.EventData;

public record EmailVerifyData(String email, String authCode) implements EventData {

}
