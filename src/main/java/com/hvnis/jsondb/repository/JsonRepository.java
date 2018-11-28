package com.hvnis.jsondb.repository;

import com.hvnis.jsondb.entity.BaseEntity;
import com.hvnis.jsondb.utils.JsonHelper;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

/**
 * @author hvnis
 */
public abstract class JsonRepository<T extends BaseEntity<ID>, ID> {
    private static final String DEFAULT_CHARSET = "UTF-8";
    private String charset;
    private Class<T> clazz;
    private String jsonFilePath;
    private List<T> dataList;

    protected JsonRepository(final String jsonFilePath) throws IOException {
        this(jsonFilePath, DEFAULT_CHARSET);
    }
    
    protected JsonRepository(final String jsonFilePath, final String charset) throws IOException {
        this.jsonFilePath = jsonFilePath;
        this.charset = charset;
        this.clazz = getGenericTypeClass();
        this.dataList = loadAll();
    }
    
    private List<T> loadAll() throws IOException {
        List<T> result = new ArrayList<T>();
        LineIterator lineIterator = null;
        try (FileInputStream fi = new FileInputStream(this.jsonFilePath);
             InputStreamReader isr = new InputStreamReader(fi, this.charset)) {
            lineIterator = new LineIterator(isr);
            while(lineIterator.hasNext()) {
                final String line = lineIterator.nextLine();
                if (StringUtils.isNotEmpty(line)) {
                    result.add(JsonHelper.parseToObject(line, this.clazz));
                }
            }
        } finally {
            if (lineIterator != null) {
                lineIterator.close();
            }
        }
        return result;
    }
    
    private void saveToFile() throws IOException {
        try (FileOutputStream fo = new FileOutputStream(this.jsonFilePath, false);
             OutputStreamWriter osw = new OutputStreamWriter(fo, this.charset)) {
            if (CollectionUtils.isNotEmpty(this.dataList)) {
                this.dataList.forEach(element -> {
                    try {
                        osw.write(JsonHelper.parseToString(element));
                        osw.write("\n");
                    } catch (IOException e) {
                        throw new RuntimeException();
                    }
                });
            } else {
                osw.write(StringUtils.EMPTY);
            }
            osw.flush();
        }
    }

    private Class<T> getGenericTypeClass() {
        try {
            String className = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
            Class<?> clazz = Class.forName(className);
            return (Class<T>) clazz;
        } catch (Exception e) {
            throw new IllegalStateException("Class is not parametrized with generic type!!! Please use extends <> ");
        }
    }
    
    public List<T> findAll() {
        return new ArrayList<T>(this.dataList);
    }

    public T findById(ID uuid) {
        if (uuid != null) {
            return this.dataList.stream().filter(element -> uuid.equals(element.getId())).findFirst().orElse(null);
        }
        return null;
    }
    
    public void save(T entity) throws IOException {
        if (entity != null) {
            final ID id = entity.getId();
            if (id != null && findById(id) == null) {
                this.dataList.add(entity);
                saveToFile();
            }
        }
    }

    public void update(T entity) throws IOException {
        if (entity != null) {
            T currentEntity = findById(entity.getId());
            if (currentEntity != null) {
                final int oldIndex = this.dataList.indexOf(currentEntity);
                this.dataList.add(oldIndex, entity);
                this.dataList.remove(currentEntity);
                saveToFile();
            }
        }
    }

    public void deleteById(ID id) throws IOException {
        if (id != null) {
            for (T element : this.dataList) {
                if (id.equals(element.getId())) {
                    this.dataList.remove(element);
                    saveToFile();
                    return;
                }
            }
        }
    }
}
