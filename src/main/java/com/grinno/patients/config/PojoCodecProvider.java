package com.grinno.patients.config;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import com.grinno.patients.model.PersistentLogin;
import com.grinno.patients.model.PersistentLoginCodec;
import com.grinno.patients.domain.UUIDStringGenerator;
import com.grinno.patients.model.User;
import com.grinno.patients.model.UserCodec;

public final class PojoCodecProvider implements CodecProvider {

    private final UUIDStringGenerator uUIDStringGenerator;

    public PojoCodecProvider() {
        this(new UUIDStringGenerator());
    }

    public PojoCodecProvider(final UUIDStringGenerator uUIDStringGenerator) {
        this.uUIDStringGenerator = uUIDStringGenerator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(final Class<T> clazz, final CodecRegistry registry) {
        if (clazz.equals(PersistentLogin.class)) {
            return (Codec<T>) new PersistentLoginCodec();
        }
        if (clazz.equals(User.class)) {
            return (Codec<T>) new UserCodec(uUIDStringGenerator);
        }
        return null;
    }
}
