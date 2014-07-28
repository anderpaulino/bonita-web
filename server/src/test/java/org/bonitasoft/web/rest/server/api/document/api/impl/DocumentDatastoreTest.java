package org.bonitasoft.web.rest.server.api.document.api.impl;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.document.Document;
import org.bonitasoft.engine.bpm.document.DocumentAttachmentException;
import org.bonitasoft.engine.bpm.document.DocumentException;
import org.bonitasoft.engine.bpm.document.DocumentNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessInstanceNotFoundException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.RetrieveException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.web.rest.model.document.DocumentItem;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DocumentDatastoreTest extends APITestWithMock {
	
	private DocumentDatastore documentDatastore;
	
	@Mock
	private WebBonitaConstantsUtils constantsValue;

	@Mock
	private APISession engineSession;

	@Mock
	private ProcessAPI processAPI;

	@Mock
	private Document mockedDocument;
	

	private DocumentItem mockedDocumentItem = new DocumentItem();
	
	@Before
    public void setUp() throws Exception {
		initMocks(this);
        System.setProperty("bonita.home", "target/bonita-home/bonita");
        when(engineSession.getTenantId()).thenReturn(1L);
        documentDatastore = spy(new DocumentDatastore(engineSession, constantsValue, processAPI));
    }
	
	@Test
	public void it_should_call_engine_processAPI_getDocument() throws Exception {
		//Given
		APIID id = APIID.makeAPIID(1l);
		
		//When
		documentDatastore.get(id);
		
		//Then
		verify(processAPI).getDocument(id.toLong());
	}
	
	@Test(expected = APIException.class)
	public void it_should_catch_and_throw_APIException_for_not_find_document() throws Exception {
		//Given
		APIID id = APIID.makeAPIID(1l);
		when(processAPI.getDocument(id.toLong())).thenThrow(new DocumentNotFoundException("not found", new Exception()));
		
		//When
		documentDatastore.get(id);
	}
	
	@Test
	public void it_should_call_convertEngineToConsole_method() throws Exception {
		//Given
		APIID id = APIID.makeAPIID(1l);
		
		//When
		documentDatastore.get(id);
		
		//Then
		verify(documentDatastore).convertEngineToConsoleItem(any(Document.class));
	}
	
	@Test
	public void it_should_convert_item_return_item() throws Exception {
		//When
		DocumentItem convertedEngineToConsoleItem = documentDatastore.convertEngineToConsoleItem(mockedDocument);
		//Then
		assertTrue(convertedEngineToConsoleItem != null);
	}
	
	@Test
	public void it_should_not_convert_null_item_return_null() {
		//When
		DocumentItem convertedEngineToConsoleItem = documentDatastore.convertEngineToConsoleItem(null);
		//Then
		assertTrue(convertedEngineToConsoleItem == null);
	}
	
	@Test
	public void it_should_add_a_document_and_call_attachDocument() throws Exception {
		//Given
		URL docUrl = getClass().getResource("/doc.jpg");
		mockedDocumentItem.setAttribute(DocumentItem.ATTRIBUTE_CASE_ID, 1l);
		mockedDocumentItem.setAttribute(DocumentItem.ATTRIBUTE_NAME, "doc 1");
		mockedDocumentItem.setAttribute(DocumentItem.ATTRIBUTE_UPLOAD_PATH, docUrl.getPath());
		//byte[] fileContent = DocumentUtil.getArrayByteFromFile(new File(docUrl));
		//When
		documentDatastore.add(mockedDocumentItem);
		
		//Then
		//verify(processAPI).attachDocument(1l, "doc 1", "doc.jpg", "img", documentContent)
		verify(documentDatastore).attachDocument(1l, "doc 1", docUrl.getPath(), "NEW_DOCUMENT");
		//throw new RuntimeException("not yet implemented");
	}
	
	@Test
	public void it_should_add_a_document_and_call_attachDocumentFromUrl() throws Exception {
		//Given
		URL docUrl = getClass().getResource("/doc.jpg");
		mockedDocumentItem.setAttribute(DocumentItem.ATTRIBUTE_CASE_ID, 1l);
		mockedDocumentItem.setAttribute(DocumentItem.ATTRIBUTE_NAME, "doc 1");
		mockedDocumentItem.setAttribute(DocumentItem.ATTRIBUTE_URL, "http://images/doc.jpg");
		//byte[] fileContent = DocumentUtil.getArrayByteFromFile(new File(docUrl));
		//When
		documentDatastore.add(mockedDocumentItem);
		
		//Then
		//verify(processAPI).attachDocument(1l, "doc 1", "doc.jpg", "img", documentContent)
		verify(documentDatastore).attachDocumentFromUrl(1l, "doc 1", "http://images/doc.jpg", "NEW_DOCUMENT");
		//throw new RuntimeException("not yet implemented");
	}
	
	@Test(expected = APIException.class)
	public void it_throws_an_exception_adding_a_document_with_invalid_inputs(){
		//Given
		mockedDocumentItem.setAttribute(DocumentItem.ATTRIBUTE_CASE_ID, -1);
		mockedDocumentItem.setAttribute(DocumentItem.ATTRIBUTE_NAME, "");
		//byte[] fileContent = DocumentUtil.getArrayByteFromFile(new File(docUrl));
		//When
		documentDatastore.add(mockedDocumentItem);
		
	}
	
	@Test
	public void it_should_call_processAPI_attachDocument_method_with_document_content() throws Exception, DocumentAttachmentException, InvalidSessionException, ProcessDefinitionNotFoundException, RetrieveException, BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException, DocumentException, IOException {
		//Given
		URL docUrl = getClass().getResource("/doc.jpg");	
		
		//When
		documentDatastore.attachDocument(1l, "Doc 1", docUrl.getPath(), "NEW_DOCUMENT");
		
		//Then
		verify(processAPI).attachDocument(1l, "Doc 1", "doc.jpg", "image/jpeg",  DocumentUtil.getArrayByteFromFile(new File(docUrl.getPath())));
	}

	
	@Test
	public void it_should_call_processAPI_attachDocument_method_with_external_url() throws Exception {
		//Given
		
		//When
		documentDatastore.attachDocumentFromUrl(1l, "Doc 1", "http://images/doc.jpg", "NEW_DOCUMENT");
		
		//Then
		verify(processAPI).attachDocument(1l, "Doc 1", "doc.jpg", "image/jpeg", "http://images/doc.jpg");
	}

	@Test
	public void it_should_call_processAPI_attachNewDocumentVersion_method_with_document_content() {
		//Given
		URL docUrl = getClass().getResource("/doc.jpg");	
		
		try {
			//When
			documentDatastore.attachDocument(1l, "Doc 1", docUrl.getPath(), "NEW_DOCUMENT_VERSION");
			//Then
			verify(processAPI).attachNewDocumentVersion(1l, "Doc 1", "doc.jpg", "image/jpeg",  DocumentUtil.getArrayByteFromFile(new File(docUrl.getPath())));
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	@Test
	public void it_should_call_processAPI_attachNewDocumentVersion_method_with_external_url() throws Exception {
		//Given
		
		//When
		documentDatastore.attachDocumentFromUrl(1l, "Doc 1", "http://images/doc.jpg", "NEW_DOCUMENT_VERSION");
		
		//Then
		verify(processAPI).attachNewDocumentVersion(1l, "Doc 1", "doc.jpg", "image/jpeg", "http://images/doc.jpg");
	}
	
	@Test
	public void it_should_call_attachDocument_with_new_document_version() {
		try {
			//Given
			Map<String, String> attributes = new HashMap<String, String>();
			attributes.put(DocumentItem.ATTRIBUTE_NAME, "Doc1");
			attributes.put(DocumentItem.ATTRIBUTE_UPLOAD_PATH, "C:\\doc.jpg");
			APIID id = APIID.makeAPIID(1l);
			when(processAPI.getDocument(1l)).thenReturn(mockedDocument);
			//When
			documentDatastore.update(id, attributes);
			//Then
			verify(documentDatastore).attachDocument(0l, "Doc1", "C:\\doc.jpg", "NEW_DOCUMENT_VERSION");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void it_should_call_attachDocumentFromUrl_with_new_document_version() {
		try {
			//Given
			Map<String, String> attributes = new HashMap<String, String>();
			attributes.put(DocumentItem.ATTRIBUTE_NAME, "Doc1");
			attributes.put(DocumentItem.ATTRIBUTE_URL, "http://images/doc.jpg");
			APIID id = APIID.makeAPIID(1l);
			when(processAPI.getDocument(1l)).thenReturn(mockedDocument);
			//When
			documentDatastore.update(id, attributes);
			//Then
			verify(documentDatastore).attachDocumentFromUrl(0l, "Doc1", "http://images/doc.jpg", "NEW_DOCUMENT_VERSION");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}