package me.suwash.gen.test;

import me.suwash.test.DefaultTestWatcher;
import me.suwash.util.RuntimeUtils;

import org.junit.runner.Description;

@lombok.extern.slf4j.Slf4j
public class GenTestWatcher extends DefaultTestWatcher {

    /* (非 Javadoc)
     * @see org.junit.rules.TestWatcher#finished(org.junit.runner.Description)
     */
    @Override
    protected void finished(Description description) {
        log.debug(RuntimeUtils.getMemoryInfo());
        super.finished(description);
    }

}
