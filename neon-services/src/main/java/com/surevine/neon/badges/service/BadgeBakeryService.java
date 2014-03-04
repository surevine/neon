package com.surevine.neon.badges.service;

import java.io.IOException;
import java.net.URL;

public interface BadgeBakeryService {

	public byte[] bake(URL source, String namespace) throws IOException;
}
