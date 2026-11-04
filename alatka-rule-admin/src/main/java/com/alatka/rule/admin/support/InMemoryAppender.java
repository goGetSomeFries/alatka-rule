package com.alatka.rule.admin.support;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class InMemoryAppender extends AppenderBase<ILoggingEvent> {

    private final StringBuilder buffer = new StringBuilder();

    @Override
    protected void append(ILoggingEvent event) {
        buffer.append(event.getFormattedMessage()).append("\n");
    }

    public void registry(Logger logger) {
        this.setContext(logger.getLoggerContext());
        logger.addAppender(this);
        this.start();
    }

    public void unRegistry(Logger logger) {
        logger.detachAppender(this);
        this.stop();
    }

    public String getLogs() {
        return buffer.toString();
    }
}
