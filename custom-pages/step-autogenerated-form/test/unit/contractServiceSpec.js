'use strict';

describe('Step Autogenerated Form test', function () {

  var $httpBackend, contractSrvc;

  beforeEach(module('autogeneratedForm'));

  beforeEach(function () {

    inject(function ($injector) {
      // Set up the mock http service responses
      $httpBackend = $injector.get('$httpBackend');
      contractSrvc = $injector.get('contractSrvc');
    });
  });

  afterEach(function () {
    $httpBackend.verifyNoOutstandingExpectation();
    $httpBackend.verifyNoOutstandingRequest();
  });

  it('should declare a fetchContract function', function () {

    expect(contractSrvc.fetchContract).toBeTruthy();
  });

  it('should return a contract', function () {

    var contract = {
      "constraints":[
        {
          "name":"ticket_comment",
          "expression":"ticket_comment!=null && !ticket_comment.toString().isEmpty()",
          "explanation":"input ticket_comment is mandatory",
          "inputNames":[
            "ticket_comment"
          ],
          "constraintType":"MANDATORY"
        }
      ],
      "complexInputs":[     ],
      "simpleInputs":[
        {
          "description":null,
          "name":"ticket_comment",
          "multiple":false,
          "type":"TEXT"
        }
      ]
    };
    var response;

    $httpBackend.expect('GET', '/bonita/API/bpm/userTask/' + 2 + '/contract')
      .respond(contract);
    contractSrvc.fetchContract(2).then(function (fetchedData) {
      response = fetchedData.data;
    });
    $httpBackend.flush();
    expect(response).toEqual(contract);
  });

});
