/*
 * Copyright (c) 2015 Uber Technologies, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.uber.tchannel.messages;

import com.uber.tchannel.headers.ArgScheme;
import com.uber.tchannel.headers.TransportHeaders;
import io.netty.buffer.ByteBuf;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ThriftRequest<T> extends EncodedRequest<T> {

    private ThriftRequest(Builder<T> builder) {
        super(builder);
    }

    protected ThriftRequest(long id, long ttl,
                          String service, Map<String, String> transportHeaders,
                          ByteBuf arg1, ByteBuf arg2, ByteBuf arg3) {
        super(id, ttl, service, transportHeaders, arg1, arg2, arg3);
    }

    /**
     * @param <T> request body type
     */
    public static class Builder<T> extends EncodedRequest.Builder<T> {

        public Builder(String service, String endpoint) {
            super(service, endpoint);
            this.transportHeaders.put(TransportHeaders.ARG_SCHEME_KEY, ArgScheme.THRIFT.getScheme());
            this.argScheme = ArgScheme.THRIFT;
        }

        public Builder(String service, ByteBuf arg1) {
            super(service, arg1);
            this.transportHeaders.put(TransportHeaders.ARG_SCHEME_KEY, ArgScheme.THRIFT.getScheme());
            this.argScheme = ArgScheme.THRIFT;
        }

        /**
         * Validates payload and populates {@link #arg1}, {@link #arg2}, {@link #arg3}.
         *
         * Args <b>>need</b> to be cleared if validation fails.
         *
         * Use {@link #release()} to clear args above.
         *
         * @throws Exception
         *     if validation fails.
         */
        @Override
        public Builder<T> validate() {
            super.validate();
            return this;
        }

        /**
         * Validates payload, populates {@link #arg1}, {@link #arg2}, {@link #arg3} and builds {@link ThriftRequest}.
         *
         * Args above will be auto-released if validation fails.
         *
         */
        public ThriftRequest<T> build() {
            ThriftRequest<T> result;
            boolean release = true;
            try {
                Builder<T> validated = this.validate();
                result = new ThriftRequest<>(validated);
                release = false;
            } finally {
                if (release) {
                    this.release();
                }
            }
            return result;
        }

        @Override
        public Builder<T> setTimeout(long timeoutMillis) {
            super.setTimeout(timeoutMillis);
            return this;
        }

        @Override
        public Builder<T> setTimeout(long timeout, TimeUnit timeUnit) {
            super.setTimeout(timeout, timeUnit);
            return this;
        }

        @Override
        public Builder<T> setId(long id) {
            super.setId(id);
            return this;
        }

        @Override
        public Builder<T> setArg2(ByteBuf arg2) {
            super.setArg2(arg2);
            return this;
        }

        @Override
        public Builder<T> setArg3(ByteBuf arg3) {
            super.setArg3(arg3);
            return this;
        }

        @Override
        public Builder<T> setHeader(String key, String value) {
            super.setHeader(key, value);
            return this;
        }

        @Override
        public Builder<T> setHeaders(Map<String, String> headers) {
            super.setHeaders(headers);
            return this;
        }

        @Override
        public Builder<T> setBody(T body) {
            super.setBody(body);
            return this;
        }

        @Override
        public Builder<T> setTransportHeader(String key, String value) {
            super.setTransportHeader(key, value);
            return this;
        }

        @Override
        public Builder<T> setTransportHeaders(Map<String, String> transportHeaders) {
            super.setTransportHeaders(transportHeaders);
            return this;
        }

        @Override
        public Builder<T> setRetryLimit(int retryLimit) {
            super.setRetryLimit(retryLimit);
            return this;
        }
    }
}
