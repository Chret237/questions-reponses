package com.alibaba.fastjson.support.spring;

import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/support/spring/PropertyPreFilters.class */
public class PropertyPreFilters {
    private List<MySimplePropertyPreFilter> filters = new ArrayList();

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/support/spring/PropertyPreFilters$MySimplePropertyPreFilter.class */
    public class MySimplePropertyPreFilter extends SimplePropertyPreFilter {
        final PropertyPreFilters this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MySimplePropertyPreFilter(PropertyPreFilters propertyPreFilters) {
            super(new String[0]);
            this.this$0 = propertyPreFilters;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MySimplePropertyPreFilter(PropertyPreFilters propertyPreFilters, Class<?> cls, String... strArr) {
            super(cls, strArr);
            this.this$0 = propertyPreFilters;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MySimplePropertyPreFilter(PropertyPreFilters propertyPreFilters, String... strArr) {
            super(strArr);
            this.this$0 = propertyPreFilters;
        }

        public MySimplePropertyPreFilter addExcludes(String... strArr) {
            for (String str : strArr) {
                getExcludes().add(str);
            }
            return this;
        }

        public MySimplePropertyPreFilter addIncludes(String... strArr) {
            for (String str : strArr) {
                getIncludes().add(str);
            }
            return this;
        }
    }

    public MySimplePropertyPreFilter addFilter() {
        MySimplePropertyPreFilter mySimplePropertyPreFilter = new MySimplePropertyPreFilter(this);
        this.filters.add(mySimplePropertyPreFilter);
        return mySimplePropertyPreFilter;
    }

    public MySimplePropertyPreFilter addFilter(Class<?> cls, String... strArr) {
        MySimplePropertyPreFilter mySimplePropertyPreFilter = new MySimplePropertyPreFilter(this, cls, strArr);
        this.filters.add(mySimplePropertyPreFilter);
        return mySimplePropertyPreFilter;
    }

    public MySimplePropertyPreFilter addFilter(String... strArr) {
        MySimplePropertyPreFilter mySimplePropertyPreFilter = new MySimplePropertyPreFilter(this, strArr);
        this.filters.add(mySimplePropertyPreFilter);
        return mySimplePropertyPreFilter;
    }

    public List<MySimplePropertyPreFilter> getFilters() {
        return this.filters;
    }

    public void setFilters(List<MySimplePropertyPreFilter> list) {
        this.filters = list;
    }

    public MySimplePropertyPreFilter[] toFilters() {
        return (MySimplePropertyPreFilter[]) this.filters.toArray(new MySimplePropertyPreFilter[0]);
    }
}
