package com.ticktalk.moreapplib;

public class MoreAppModel {

    private String appName;
    private String description;
    private String packageName;
    private int icon;
    private int background;
    private boolean free;

    private MoreAppModel(String appName, String description, String packageName, int icon, int background, boolean free)
    {
        this.appName = appName;
        this.description = description;
        this.packageName = packageName;
        this.icon = icon;
        this.background = background;
        this.free = free;
    }

    public String getAppName() {
        return appName;
    }

    public String getDescription() {
        return description;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getIcon() {
        return icon;
    }

    public int getBackground() {
        return background;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public boolean isFree() {
        return free;
    }

    public static class Builder
    {
        private String appName;
        private String description;
        private String packageName;
        private int icon;
        private int background;
        private boolean free = true;

        public Builder(){}

        public Builder appName(String appName)
        {
            this.appName = appName;
            return this;
        }

        public Builder description(String description)
        {
            this.description = description;
            return this;
        }

        public Builder packageName(String packageName)
        {
            this.packageName = packageName;
            return this;
        }

        public Builder icon(int icon)
        {
            this.icon = icon;
            return this;
        }

        public Builder background(int background)
        {
            this.background = background;
            return this;
        }

        public Builder free(boolean free)
        {
            this.free = free;
            return this;
        }

        public MoreAppModel build()
        {
            return new MoreAppModel(appName, description, packageName, icon, background, free);
        }
    }
}
