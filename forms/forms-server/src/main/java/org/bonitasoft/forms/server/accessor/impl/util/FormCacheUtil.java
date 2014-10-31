package org.bonitasoft.forms.server.accessor.impl.util;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.forms.client.model.ApplicationConfig;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormAction;
import org.bonitasoft.forms.client.model.FormPage;
import org.bonitasoft.forms.client.model.FormValidator;
import org.bonitasoft.forms.client.model.FormWidget;
import org.bonitasoft.forms.client.model.TransientData;
import org.bonitasoft.forms.server.cache.CacheUtil;

public class FormCacheUtil {

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(FormCacheUtil.class.getName());

    protected static final String FORM_APPLICATION_PERMISSIONS_CACHE = "formApplicationPermissionsCache";

    protected static final String FORM_MIGRATION_PRODUCT_VERSION_CACHE = "formApplicationMigrationProductVersionCache";

    protected static final String FORM_APPLICATION_VERSION_CACHE = "formApplicationVersionCache";

    protected static final String FORM_APPLICATION_NAME_CACHE = "formApplicationNameCache";

    protected static final String FORM_PERMISSIONS_CACHE = "formPermissionsCache";

    protected static final String FORM_NEXT_FORM_CACHE = "formNextFormCache";

    protected static final String FORM_PAGE_LAYOUT_CACHE = "formPageLayoutCache";

    protected static final String FORM_APPLICATION_LAYOUT_CACHE = "formApplicationLayoutCache";

    protected static final String FORM_FIRST_PAGE_CACHE = "formFirstPageCache";

    protected static final String FORM_PAGES_CACHE = "formPagesCache";

    protected static final String FORM_TRANSIENT_DATA_CACHE = "formTransientDataCache";

    protected static final String FORM_PAGE_ACTIONS_CACHE = "formActionsCache";

    protected static final String FORM_CONFIG_CACHE = "formConfigCache";

    protected static final String NEXT_PAGE_ID_EXPRESSION_CACHE = "nextPageIdExpressionCache";

    protected static final String FIELD_VALIDATORS_CACHE = "fieldValidatorsCache";

    protected static final String PAGE_VALIDATORS_CACHE = "pageValidatorsCache";

    protected static final String FORM_WIDGET_CACHE = "formWidgetCache";

    protected static String CACHE_DISK_STORE_PATH = null;

    protected static String DOMAIN_KEY_CONNECTOR = "@";

    protected long tenantID;

    protected FormCacheUtil(final long tenantID) {
        try {
            CACHE_DISK_STORE_PATH = WebBonitaConstantsUtils.getInstance(tenantID).getFormsWorkFolder().getAbsolutePath();
            this.tenantID = tenantID;
        } catch (final Exception e) {
            LOGGER.log(Level.WARNING, "Unable to retrieve the path of the cache disk store directory path.", e);
        }
    }

    protected static int getContextClassLoaderHash() {
        return Thread.currentThread().getContextClassLoader().hashCode();
    }

    protected static String getDateStr(final Date date) {
        if (date != null) {
            return Long.toString(date.getTime());
        }
        return "";
    }

    public Expression getFirstPage(final String formID, final String locale, final Date applicationDeployementDate) {
        return (Expression) CacheUtil.get(FORM_FIRST_PAGE_CACHE, getContextClassLoaderHash() + formID + locale + getDateStr(applicationDeployementDate)
                + DOMAIN_KEY_CONNECTOR + tenantID);
    }

    public void storeFirstPage(final String formID, final String locale, final Date applicationDeployementDate, final Expression firstPage) {
        CacheUtil.store(FORM_FIRST_PAGE_CACHE, CACHE_DISK_STORE_PATH, getContextClassLoaderHash() + formID + locale + getDateStr(applicationDeployementDate)
                + DOMAIN_KEY_CONNECTOR + tenantID, firstPage);
    }

    public FormPage getPage(final String formID, final String locale, final Date applicationDeployementDate, final String pageId) {
        return (FormPage) CacheUtil.get(FORM_PAGES_CACHE, getContextClassLoaderHash() + formID + locale + getDateStr(applicationDeployementDate) + pageId
                + DOMAIN_KEY_CONNECTOR + tenantID);
    }

    public void storePage(final String formID, final String locale, final Date applicationDeployementDate, final FormPage formPage) {
        CacheUtil.store(FORM_PAGES_CACHE, CACHE_DISK_STORE_PATH, getContextClassLoaderHash() + formID + locale + getDateStr(applicationDeployementDate)
                + formPage.getPageId() + DOMAIN_KEY_CONNECTOR + tenantID, formPage);
    }

    @SuppressWarnings("unchecked")
    public List<TransientData> getTransientData(final String formID, final String locale, final Date applicationDeployementDate) {
        return (List<TransientData>) CacheUtil.get(FORM_TRANSIENT_DATA_CACHE, getContextClassLoaderHash() + formID + locale
                + getDateStr(applicationDeployementDate) + DOMAIN_KEY_CONNECTOR + tenantID);
    }

    public void storeTransientData(final String formID, final String locale, final Date applicationDeployementDate, final List<TransientData> transientData) {
        CacheUtil.store(FORM_TRANSIENT_DATA_CACHE, CACHE_DISK_STORE_PATH, getContextClassLoaderHash() + formID + locale
                + getDateStr(applicationDeployementDate) + DOMAIN_KEY_CONNECTOR + tenantID, transientData);
    }

    @SuppressWarnings("unchecked")
    public List<FormAction> getPageActions(final String formID, final String locale, final Date applicationDeployementDate, final String activityName,
            final String pageId) {
        return (List<FormAction>) CacheUtil.get(FORM_PAGE_ACTIONS_CACHE, getContextClassLoaderHash() + formID + locale + getDateStr(applicationDeployementDate)
                + activityName + pageId + DOMAIN_KEY_CONNECTOR + tenantID);
    }

    public void storePageActions(final String formID, final String locale, final Date applicationDeployementDate, final String activityName,
            final String pageId, final List<FormAction> actions) {
        CacheUtil.store(FORM_PAGE_ACTIONS_CACHE, CACHE_DISK_STORE_PATH, getContextClassLoaderHash() + formID + locale + getDateStr(applicationDeployementDate)
                + activityName + pageId + DOMAIN_KEY_CONNECTOR + tenantID, actions);
    }

    public ApplicationConfig getApplicationConfig(final String formID, final String locale, final Date applicationDeployementDate,
            final boolean includeApplicationTemplate) {
        return (ApplicationConfig) CacheUtil.get(FORM_CONFIG_CACHE, getContextClassLoaderHash() + formID + locale + getDateStr(applicationDeployementDate)
                + Boolean.toString(includeApplicationTemplate) + DOMAIN_KEY_CONNECTOR + tenantID);
    }

    public final void storeApplicationConfig(final String formID, final String locale, final Date applicationDeployementDate,
            final boolean includeApplicationTemplate, final ApplicationConfig ApplicationConfig) {
        CacheUtil.store(FORM_CONFIG_CACHE, CACHE_DISK_STORE_PATH, getContextClassLoaderHash() + formID + locale + getDateStr(applicationDeployementDate)
                + Boolean.toString(includeApplicationTemplate) + DOMAIN_KEY_CONNECTOR + tenantID, ApplicationConfig);
    }

    public String getApplicationPermissions(final String formID, final String locale, final Date applicationDeployementDate) {
        return (String) CacheUtil.get(FORM_APPLICATION_PERMISSIONS_CACHE, getContextClassLoaderHash() + formID + locale
                + getDateStr(applicationDeployementDate) + DOMAIN_KEY_CONNECTOR + tenantID);
    }

    public void storeApplicationPermissions(final String formID, final String locale, final Date applicationDeployementDate, final String applicationPermissions) {
        CacheUtil.store(FORM_APPLICATION_PERMISSIONS_CACHE, CACHE_DISK_STORE_PATH, getContextClassLoaderHash() + formID + locale
                + getDateStr(applicationDeployementDate) + DOMAIN_KEY_CONNECTOR + tenantID, applicationPermissions);
    }

    public String getMigrationProductVersion(final String formID, final String locale, final Date applicationDeployementDate) {
        return (String) CacheUtil.get(FORM_MIGRATION_PRODUCT_VERSION_CACHE, getContextClassLoaderHash() + formID + locale
                + getDateStr(applicationDeployementDate) + DOMAIN_KEY_CONNECTOR + tenantID);
    }

    public void storeMigrationProductVersion(final String formID, final String locale, final Date applicationDeployementDate,
            final String migrationProductVersion) {
        CacheUtil.store(FORM_MIGRATION_PRODUCT_VERSION_CACHE, CACHE_DISK_STORE_PATH, getContextClassLoaderHash() + formID + locale
                + getDateStr(applicationDeployementDate) + DOMAIN_KEY_CONNECTOR + tenantID, migrationProductVersion);
    }

    public String getFormPermissions(final String formID, final String locale, final Date applicationDeployementDate) {
        return (String) CacheUtil.get(FORM_PERMISSIONS_CACHE, getContextClassLoaderHash() + formID + locale + getDateStr(applicationDeployementDate)
                + DOMAIN_KEY_CONNECTOR + tenantID);
    }

    public void storeFormPermissions(final String formID, final String locale, final Date applicationDeployementDate, final String formPermissions) {
        CacheUtil.store(FORM_PERMISSIONS_CACHE, CACHE_DISK_STORE_PATH, getContextClassLoaderHash() + formID + locale + getDateStr(applicationDeployementDate)
                + DOMAIN_KEY_CONNECTOR + tenantID, formPermissions);
    }

    public String getNextForm(final String formID, final String locale, final Date applicationDeployementDate) {
        return (String) CacheUtil.get(FORM_NEXT_FORM_CACHE, getContextClassLoaderHash() + formID + locale + getDateStr(applicationDeployementDate)
                + DOMAIN_KEY_CONNECTOR + tenantID);
    }

    public void storeNextForm(final String formID, final String locale, final Date applicationDeployementDate, final String nextForm) {
        CacheUtil.store(FORM_NEXT_FORM_CACHE, CACHE_DISK_STORE_PATH, getContextClassLoaderHash() + formID + locale + getDateStr(applicationDeployementDate)
                + DOMAIN_KEY_CONNECTOR + tenantID, nextForm);
    }

    public String getFormPageLayout(final String formID, final String locale, final Date applicationDeployementDate, final String pageId) {
        return (String) CacheUtil.get(FORM_PAGE_LAYOUT_CACHE, getContextClassLoaderHash() + formID + locale + getDateStr(applicationDeployementDate) + pageId
                + DOMAIN_KEY_CONNECTOR + tenantID);
    }

    public void storeFormPageLayout(final String formID, final String locale, final Date applicationDeployementDate, final String pageId,
            final String formPageLayout) {
        CacheUtil.store(FORM_PAGE_LAYOUT_CACHE, CACHE_DISK_STORE_PATH, getContextClassLoaderHash() + formID + locale + getDateStr(applicationDeployementDate)
                + pageId + DOMAIN_KEY_CONNECTOR + tenantID, formPageLayout);
    }

    public String getApplicationVersion(final String formID, final String locale, final Date applicationDeployementDate) {
        return (String) CacheUtil.get(FORM_APPLICATION_VERSION_CACHE, getContextClassLoaderHash() + formID + locale + getDateStr(applicationDeployementDate)
                + DOMAIN_KEY_CONNECTOR + tenantID);
    }

    public void storeApplicationVersion(final String formID, final String locale, final Date applicationDeployementDate, final String applicationVersion) {
        CacheUtil.store(FORM_APPLICATION_VERSION_CACHE, CACHE_DISK_STORE_PATH, getContextClassLoaderHash() + formID + locale
                + getDateStr(applicationDeployementDate) + DOMAIN_KEY_CONNECTOR + tenantID, applicationVersion);
    }

    public String getApplicationName(final String formID, final String locale, final Date applicationDeployementDate) {
        return (String) CacheUtil.get(FORM_APPLICATION_NAME_CACHE, getContextClassLoaderHash() + formID + locale + getDateStr(applicationDeployementDate)
                + DOMAIN_KEY_CONNECTOR + tenantID);
    }

    public void storeApplicationName(final String formID, final String locale, final Date applicationDeployementDate, final String applicationName) {
        CacheUtil.store(FORM_APPLICATION_NAME_CACHE, CACHE_DISK_STORE_PATH, getContextClassLoaderHash() + formID + locale
                + getDateStr(applicationDeployementDate) + DOMAIN_KEY_CONNECTOR + tenantID, applicationName);
    }

    public FormWidget getFormWidget(final String formWidgetCacheId) {
        return (FormWidget) CacheUtil.get(FORM_WIDGET_CACHE, formWidgetCacheId);
    }

    public String storeFormWidget(final String formID, final String pageID, final String locale, final Date processDeployementDate, final FormWidget formWidget) {
        final String formWidgetCacheId = getContextClassLoaderHash() + formID + pageID + formWidget.getId() + locale + getDateStr(processDeployementDate)
                + DOMAIN_KEY_CONNECTOR + tenantID;
        CacheUtil.store(FORM_WIDGET_CACHE, CACHE_DISK_STORE_PATH, formWidgetCacheId, formWidget);
        return formWidgetCacheId;
    }

    public Expression getNextPageIdExpression(final String nextPageExpressionId) {
        return (Expression) CacheUtil.get(NEXT_PAGE_ID_EXPRESSION_CACHE, nextPageExpressionId);
    }

    public String storeNextPageIdExpression(final String formID, final String pageID, final String locale, final Date processDeployementDate,
            final Expression nextPageIdExpression) {
        final String nextPageExpressionId = getContextClassLoaderHash() + formID + pageID + locale + getDateStr(processDeployementDate) + DOMAIN_KEY_CONNECTOR
                + tenantID;
        CacheUtil.store(NEXT_PAGE_ID_EXPRESSION_CACHE, CACHE_DISK_STORE_PATH, nextPageExpressionId, nextPageIdExpression);
        return nextPageExpressionId;
    }

    @SuppressWarnings("unchecked")
    public List<FormValidator> getFieldValidators(final String fieldValidatorsId) {
        return (List<FormValidator>) CacheUtil.get(FIELD_VALIDATORS_CACHE, fieldValidatorsId);
    }

    public String storeFieldValidators(final String formID, final String pageID, final String widgetID, final String locale, final Date processDeployementDate,
            final List<FormValidator> validators) {
        final String validatorsId = getContextClassLoaderHash() + formID + pageID + widgetID + locale + getDateStr(processDeployementDate)
                + DOMAIN_KEY_CONNECTOR + tenantID;
        CacheUtil.store(FIELD_VALIDATORS_CACHE, CACHE_DISK_STORE_PATH, validatorsId, validators);
        return validatorsId;
    }

    @SuppressWarnings("unchecked")
    public List<FormValidator> getPageValidators(final String pageValidatorsId) {
        return (List<FormValidator>) CacheUtil.get(PAGE_VALIDATORS_CACHE, pageValidatorsId);
    }

    public String storePageValidators(final String formID, final String pageID, final String locale, final Date processDeployementDate,
            final List<FormValidator> validators) {
        final String validatorsId = getContextClassLoaderHash() + formID + pageID + locale + getDateStr(processDeployementDate) + DOMAIN_KEY_CONNECTOR
                + tenantID;
        CacheUtil.store(PAGE_VALIDATORS_CACHE, CACHE_DISK_STORE_PATH, validatorsId, validators);
        return validatorsId;
    }

    public String getPageLayoutContent(final String bodyContentId) {
        final String bodyContent = (String) CacheUtil.get(FORM_PAGE_LAYOUT_CACHE, bodyContentId);
        return bodyContent;
    }

    public String storePageLayoutContent(final String formID, final String PageID, final String locale, final Date applicationDeployementDate,
            final String BodyContent) {
        final String bodyContentId = getContextClassLoaderHash() + formID + PageID + locale + getDateStr(applicationDeployementDate) + DOMAIN_KEY_CONNECTOR
                + tenantID;
        CacheUtil.store(FORM_PAGE_LAYOUT_CACHE, CACHE_DISK_STORE_PATH, bodyContentId, BodyContent);
        return bodyContentId;
    }

    public String getApplicationLayoutContent(final String bodyContentId) {
        final String bodyContent = (String) CacheUtil.get(FORM_APPLICATION_LAYOUT_CACHE, bodyContentId);
        return bodyContent;
    }

    public String storeApplicationLayoutContent(final String formID, final String locale, final Date applicationDeployementDate, final String BodyContent) {
        final String bodyContentId = getContextClassLoaderHash() + formID + locale + getDateStr(applicationDeployementDate) + DOMAIN_KEY_CONNECTOR + tenantID;
        CacheUtil.store(FORM_APPLICATION_LAYOUT_CACHE, CACHE_DISK_STORE_PATH, bodyContentId, BodyContent);
        return bodyContentId;
    }

    public void clearAll() {
        CacheUtil.clear(FORM_FIRST_PAGE_CACHE);
        CacheUtil.clear(FORM_PAGES_CACHE);
        CacheUtil.clear(FORM_TRANSIENT_DATA_CACHE);
        CacheUtil.clear(FORM_PAGE_ACTIONS_CACHE);
        CacheUtil.clear(FORM_CONFIG_CACHE);
        CacheUtil.clear(FORM_APPLICATION_PERMISSIONS_CACHE);
        CacheUtil.clear(FORM_MIGRATION_PRODUCT_VERSION_CACHE);
        CacheUtil.clear(FORM_PERMISSIONS_CACHE);
        CacheUtil.clear(FORM_NEXT_FORM_CACHE);
        CacheUtil.clear(FORM_PAGE_LAYOUT_CACHE);
        CacheUtil.clear(FORM_APPLICATION_VERSION_CACHE);
        CacheUtil.clear(FORM_APPLICATION_NAME_CACHE);
        CacheUtil.clear(NEXT_PAGE_ID_EXPRESSION_CACHE);
        CacheUtil.clear(FIELD_VALIDATORS_CACHE);
        CacheUtil.clear(PAGE_VALIDATORS_CACHE);
        CacheUtil.clear(FORM_WIDGET_CACHE);
        CacheUtil.clear(FORM_APPLICATION_LAYOUT_CACHE);
    }

}
