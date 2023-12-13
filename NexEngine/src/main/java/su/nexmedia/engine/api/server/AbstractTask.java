package su.nexmedia.engine.api.server;

import org.jetbrains.annotations.NotNull;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import su.nexmedia.engine.NexPlugin;

public abstract class AbstractTask<P extends NexPlugin<P>> {

    @NotNull protected final P plugin;

    protected WrappedTask taskId = null;
    protected long    interval;
    protected boolean async;

    public AbstractTask(@NotNull P plugin, int interval, boolean async) {
        this(plugin, interval * 20L, async);
    }

    public AbstractTask(@NotNull P plugin, long interval, boolean async) {
        this.plugin = plugin;
        this.interval = interval;
        this.async = async;
    }

    public abstract void action();

    public final void restart() {
        this.stop();
        this.start();
    }

    public boolean start() {
        if (this.taskId != null) return false;
        if (this.interval <= 0L) return false;

        if (this.async) {
            this.taskId = plugin.getFoliaLib().getImpl().runTimerAsync(this::action, 1L, interval);
        }
        else {
            this.taskId = plugin.getFoliaLib().getImpl().runTimer(this::action, 1L, interval);
        }
        return true;
    }

    public boolean stop() {
        if (this.taskId == null) return false;

        taskId.cancel();
        this.taskId = null;
        return true;
    }
}
