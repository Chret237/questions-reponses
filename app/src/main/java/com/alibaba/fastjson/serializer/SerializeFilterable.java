package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/serializer/SerializeFilterable.class */
public abstract class SerializeFilterable {
    protected List<BeforeFilter> beforeFilters = null;
    protected List<AfterFilter> afterFilters = null;
    protected List<PropertyFilter> propertyFilters = null;
    protected List<ValueFilter> valueFilters = null;
    protected List<NameFilter> nameFilters = null;
    protected List<PropertyPreFilter> propertyPreFilters = null;
    protected List<LabelFilter> labelFilters = null;
    protected List<ContextValueFilter> contextValueFilters = null;
    protected boolean writeDirect = true;

    public void addFilter(SerializeFilter serializeFilter) {
        if (serializeFilter == null) {
            return;
        }
        if (serializeFilter instanceof PropertyPreFilter) {
            getPropertyPreFilters().add((PropertyPreFilter) serializeFilter);
        }
        if (serializeFilter instanceof NameFilter) {
            getNameFilters().add((NameFilter) serializeFilter);
        }
        if (serializeFilter instanceof ValueFilter) {
            getValueFilters().add((ValueFilter) serializeFilter);
        }
        if (serializeFilter instanceof ContextValueFilter) {
            getContextValueFilters().add((ContextValueFilter) serializeFilter);
        }
        if (serializeFilter instanceof PropertyFilter) {
            getPropertyFilters().add((PropertyFilter) serializeFilter);
        }
        if (serializeFilter instanceof BeforeFilter) {
            getBeforeFilters().add((BeforeFilter) serializeFilter);
        }
        if (serializeFilter instanceof AfterFilter) {
            getAfterFilters().add((AfterFilter) serializeFilter);
        }
        if (serializeFilter instanceof LabelFilter) {
            getLabelFilters().add((LabelFilter) serializeFilter);
        }
    }

    public boolean apply(JSONSerializer jSONSerializer, Object obj, String str, Object obj2) {
        if (jSONSerializer.propertyFilters != null) {
            Iterator<PropertyFilter> it = jSONSerializer.propertyFilters.iterator();
            while (it.hasNext()) {
                if (!it.next().apply(obj, str, obj2)) {
                    return false;
                }
            }
        }
        List<PropertyFilter> list = this.propertyFilters;
        if (list == null) {
            return true;
        }
        Iterator<PropertyFilter> it2 = list.iterator();
        while (it2.hasNext()) {
            if (!it2.next().apply(obj, str, obj2)) {
                return false;
            }
        }
        return true;
    }

    public boolean applyName(JSONSerializer jSONSerializer, Object obj, String str) {
        if (jSONSerializer.propertyPreFilters != null) {
            Iterator<PropertyPreFilter> it = jSONSerializer.propertyPreFilters.iterator();
            while (it.hasNext()) {
                if (!it.next().apply(jSONSerializer, obj, str)) {
                    return false;
                }
            }
        }
        List<PropertyPreFilter> list = this.propertyPreFilters;
        if (list == null) {
            return true;
        }
        Iterator<PropertyPreFilter> it2 = list.iterator();
        while (it2.hasNext()) {
            if (!it2.next().apply(jSONSerializer, obj, str)) {
                return false;
            }
        }
        return true;
    }

    public List<AfterFilter> getAfterFilters() {
        if (this.afterFilters == null) {
            this.afterFilters = new ArrayList();
            this.writeDirect = false;
        }
        return this.afterFilters;
    }

    public List<BeforeFilter> getBeforeFilters() {
        if (this.beforeFilters == null) {
            this.beforeFilters = new ArrayList();
            this.writeDirect = false;
        }
        return this.beforeFilters;
    }

    public List<ContextValueFilter> getContextValueFilters() {
        if (this.contextValueFilters == null) {
            this.contextValueFilters = new ArrayList();
            this.writeDirect = false;
        }
        return this.contextValueFilters;
    }

    public List<LabelFilter> getLabelFilters() {
        if (this.labelFilters == null) {
            this.labelFilters = new ArrayList();
            this.writeDirect = false;
        }
        return this.labelFilters;
    }

    public List<NameFilter> getNameFilters() {
        if (this.nameFilters == null) {
            this.nameFilters = new ArrayList();
            this.writeDirect = false;
        }
        return this.nameFilters;
    }

    public List<PropertyFilter> getPropertyFilters() {
        if (this.propertyFilters == null) {
            this.propertyFilters = new ArrayList();
            this.writeDirect = false;
        }
        return this.propertyFilters;
    }

    public List<PropertyPreFilter> getPropertyPreFilters() {
        if (this.propertyPreFilters == null) {
            this.propertyPreFilters = new ArrayList();
            this.writeDirect = false;
        }
        return this.propertyPreFilters;
    }

    public List<ValueFilter> getValueFilters() {
        if (this.valueFilters == null) {
            this.valueFilters = new ArrayList();
            this.writeDirect = false;
        }
        return this.valueFilters;
    }

    protected String processKey(JSONSerializer jSONSerializer, Object obj, String str, Object obj2) {
        String strProcess = str;
        if (jSONSerializer.nameFilters != null) {
            Iterator<NameFilter> it = jSONSerializer.nameFilters.iterator();
            while (true) {
                strProcess = str;
                if (!it.hasNext()) {
                    break;
                }
                str = it.next().process(obj, str, obj2);
            }
        }
        List<NameFilter> list = this.nameFilters;
        String str2 = strProcess;
        if (list != null) {
            Iterator<NameFilter> it2 = list.iterator();
            while (true) {
                str2 = strProcess;
                if (!it2.hasNext()) {
                    break;
                }
                strProcess = it2.next().process(obj, strProcess, obj2);
            }
        }
        return str2;
    }

    protected Object processValue(JSONSerializer jSONSerializer, BeanContext beanContext, Object obj, String str, Object obj2) {
        return processValue(jSONSerializer, beanContext, obj, str, obj2, 0);
    }

    protected Object processValue(JSONSerializer jSONSerializer, BeanContext beanContext, Object obj, String str, Object obj2, int i) {
        boolean z;
        Object objProcess = obj2;
        if (obj2 != null) {
            if ((SerializerFeature.isEnabled(jSONSerializer.out.features, i, SerializerFeature.WriteNonStringValueAsString) || !(beanContext == null || (beanContext.getFeatures() & SerializerFeature.WriteNonStringValueAsString.mask) == 0)) && (((z = obj2 instanceof Number)) || (obj2 instanceof Boolean))) {
                String format = null;
                if (z) {
                    format = null;
                    if (beanContext != null) {
                        format = beanContext.getFormat();
                    }
                }
                objProcess = format != null ? new DecimalFormat(format).format(obj2) : obj2.toString();
            } else {
                objProcess = obj2;
                if (beanContext != null) {
                    objProcess = obj2;
                    if (beanContext.isJsonDirect()) {
                        objProcess = JSON.parse((String) obj2);
                    }
                }
            }
        }
        Object objProcess2 = objProcess;
        if (jSONSerializer.valueFilters != null) {
            Iterator<ValueFilter> it = jSONSerializer.valueFilters.iterator();
            while (true) {
                objProcess2 = objProcess;
                if (!it.hasNext()) {
                    break;
                }
                objProcess = it.next().process(obj, str, objProcess);
            }
        }
        List<ValueFilter> list = this.valueFilters;
        Object objProcess3 = objProcess2;
        if (list != null) {
            Iterator<ValueFilter> it2 = list.iterator();
            while (true) {
                objProcess3 = objProcess2;
                if (!it2.hasNext()) {
                    break;
                }
                objProcess2 = it2.next().process(obj, str, objProcess2);
            }
        }
        Object objProcess4 = objProcess3;
        if (jSONSerializer.contextValueFilters != null) {
            Iterator<ContextValueFilter> it3 = jSONSerializer.contextValueFilters.iterator();
            while (true) {
                objProcess4 = objProcess3;
                if (!it3.hasNext()) {
                    break;
                }
                objProcess3 = it3.next().process(beanContext, obj, str, objProcess3);
            }
        }
        List<ContextValueFilter> list2 = this.contextValueFilters;
        Object obj3 = objProcess4;
        if (list2 != null) {
            Iterator<ContextValueFilter> it4 = list2.iterator();
            while (true) {
                obj3 = objProcess4;
                if (!it4.hasNext()) {
                    break;
                }
                objProcess4 = it4.next().process(beanContext, obj, str, objProcess4);
            }
        }
        return obj3;
    }

    protected boolean writeDirect(JSONSerializer jSONSerializer) {
        return jSONSerializer.out.writeDirect && this.writeDirect && jSONSerializer.writeDirect;
    }
}
