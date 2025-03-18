package seeders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

//public class PerfilSeeder implements ITipoSeeder {

   // @Override
    //public void seed(Connection connection) {
        // insere perfis
       /* String sqlCampo = """
                    INSERT INTO perfil(idPerfil, nomePerfil, perfilBackend, created_atPerfil, usuario_idUsuario) VALUES 
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'Android', 0, '2000-01-01 00:00:00', '0d1f767d-46d7-401b-922b-ff1a063ce661'),
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'iOS', 0, '2000-01-01 00:00:00', '0d1f767d-46d7-401b-922b-ff1a063ce661'),
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'Web/Back End', 0, '2000-01-01 00:00:00', '0d1f767d-46d7-401b-922b-ff1a063ce661'),
                        
                    ;
                """;*/
        /*
        String sqlPerfilCampo = """ 
            INSERT INTO perfil_has_campo(perfil_idPerfil, campo_idCampo, diasPerfil) VALUES 
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'b4a901a7-074b-4ad1-bf0a-7c076849f2db', 10),
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '2f2857c7-4bfc-4f05-aeb9-4bf5900023ed', '30') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '149a395a-de3d-44e7-b87d-92c73eccc416', '50') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '9b909ee6-0329-4e44-bbe6-59231bdf070a', '30') , 
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '45788a7a-526b-4ee8-bf76-489f6f6eaeb6', '50') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'fb461e7d-daa9-4128-91c3-bbb6cfa84be1', '70') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '47fe3e3d-35a6-462e-b1ef-ccfe397710d6', '1') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '3ac6996a-0912-4c46-8867-ce2480465858', '2') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '34fc6bef-5849-4f68-86e6-e4d8b969873e', '2') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '2687cc9f-3a9a-46ec-9d29-4df4bd096c4a', '2') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '8b21d504-b8a7-411c-bf68-960e838df870', '2') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'd9e889d7-d6f5-41dc-a7f2-bdd495a7d1b5', '2') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '9ceb34d6-af2e-4e29-ab8d-6e5ee5a071bb', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '0245fdfd-5780-44eb-9a88-23fdbd00e038', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'bdc6a3f8-3f53-4781-abb3-f5f4994d8420', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '73ff7cfe-ef3d-401b-9124-0b53ba4c2569', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'bfbf94d1-206f-49a1-873b-817a60422e49', '5') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'bb0b847b-197e-4032-8a96-ef758bbcb76c', '4') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '9e61a042-7015-4af2-a132-802489c7ec1b', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '992ba03d-5239-4a0a-8443-ba548b6e6431', '4') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '12627d49-1ed7-492a-a188-6ef25ac4dbef', '2') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '100d947e-2ec2-4b89-ad92-e80603a7bbd3', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '7f576a1d-4807-4ddd-9f8f-1e22d56e226b', '2') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'aecfba78-2cd3-4090-8294-de3b4360b9d4', '5') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '6adb8628-4591-44b3-ae48-7469e75e314c', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'db579a4a-c13c-400c-8cf7-0b38ed0d7aeb', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '40f33e98-21c8-4bcc-a976-d7254ff12dbd', '3') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '91e390ea-897d-4378-b14b-0c130b0255b8', '6') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '038002d8-e5cb-4335-9c17-d2a31572a8e1', '3') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '46a35e5d-7756-4730-8476-ee86032f137e', '3') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '879fcda7-dff0-488d-9877-ad37de3c43b8', '5') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'c7d9ad75-622f-4759-bfaa-2d9e9148d1f3', '5') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '750de3e9-d0a2-4abf-863f-cb6c4ad39e0f', '5') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'c85849f3-4b68-4f00-b905-3d2979d22e80', '1') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '1cd55c47-b212-4589-856f-fdf143f34b88', '3') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'b93f58f1-67a5-41d6-8100-4234061e5689', '3') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '28873670-b713-4ea9-814c-e3ecdf107cf4', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '94e706b2-d6db-4414-aa78-94cc95117fc9', '5') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'def9db92-aaf5-4696-bef1-6ed3949d3264', '5') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '4f6c9f8b-fd04-4176-b618-901b24730a23', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '280a43a3-6168-46a8-912c-86647bbf08f7', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '0b4624a3-aaf3-4415-81e2-304db4422645', '5') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '555ada22-a0bd-4ba1-9aa3-cf63a100de38', '3') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'c8170052-4a94-454f-bc92-2471721ec4bf', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '4f527130-7c02-499b-81d8-81f2c2c49705', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '028eed0e-5e6c-4bdf-a4f7-fd1070c9de4d', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'c89fd872-a33c-4983-a6b5-172cd6148d96', '3') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '04bf538c-b8db-41d8-a1f8-fe175d4cd0ef', '3') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'd2bc6f27-1cec-4e2b-a362-6f7f6080a8da', '1') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '26c362cf-bea7-4ead-9fd6-19200956cd63', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'c2111c79-3c27-4b92-b928-50086d0beebe', '4') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '812bb368-e39f-4988-9764-8656d9cf59b8', '3') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'c50c4e4d-7cdb-44b0-b9ec-62ea48c94a41', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '9013d1b3-a273-404b-8000-d7440a5dfc43', '4') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '133fdc79-6981-4481-95fa-218688bf5b32', '4') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '9abc800f-cc69-46d1-843a-6b5d6226eaf0', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'f0b42345-650e-4e86-8554-c030f2b9d023', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'b0f40f48-283f-4f34-bea9-f8e9b912a6b9', '5') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '3ce27e7f-abfc-4102-92df-39fbede6c42f', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '7df55551-db82-46f3-a26e-5f37fa6af3c4', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'b8689954-f746-4f5e-b83e-ecdb0cec5f4a', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'a57bc71a-cf4c-41d7-9793-bc630794533e', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '201ade80-031a-43d5-ba10-dc86312084f6', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '1f9bee54-60e5-4780-9a90-6a0bd565b873', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'a2d4a9a5-a172-49d4-b75c-2bee72194531', '0') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '592f7684-30e9-4125-b5b0-877e613bde7a', '7') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '3a15e85e-74f3-4ff2-98d2-61c19bab07eb', '5') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'a8a1af03-e325-453f-83d5-1f53d817b3fb', '5') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '9247327a-a523-461e-8f0a-cf7de5bd6ee9', '2') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '32d0e1a2-466c-4cff-aea5-7828e75b4eef', '4') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'ef8e2c74-b58e-4ae2-9b6a-cb16f26194d9', '10') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '0c020c36-40de-4b25-9900-820873be8119', '550') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', 'f5097bc7-c9f9-44e8-a292-075f2f9d2047', '300') ,
                        ('a061304a-9a66-4bb4-a6db-1d6b9582cdee', '93606340-6b27-48f8-8109-07d657421834', '450') , 
                                
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'b4a901a7-074b-4ad1-bf0a-7c076849f2db', 10),
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '2f2857c7-4bfc-4f05-aeb9-4bf5900023ed', '30') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '149a395a-de3d-44e7-b87d-92c73eccc416', '50') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '9b909ee6-0329-4e44-bbe6-59231bdf070a', '30') , 
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '45788a7a-526b-4ee8-bf76-489f6f6eaeb6', '50') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'fb461e7d-daa9-4128-91c3-bbb6cfa84be1', '70') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '47fe3e3d-35a6-462e-b1ef-ccfe397710d6', '1') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '3ac6996a-0912-4c46-8867-ce2480465858', '2') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '34fc6bef-5849-4f68-86e6-e4d8b969873e', '2') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '2687cc9f-3a9a-46ec-9d29-4df4bd096c4a', '2') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '8b21d504-b8a7-411c-bf68-960e838df870', '2') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'd9e889d7-d6f5-41dc-a7f2-bdd495a7d1b5', '2') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '9ceb34d6-af2e-4e29-ab8d-6e5ee5a071bb', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '0245fdfd-5780-44eb-9a88-23fdbd00e038', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'bdc6a3f8-3f53-4781-abb3-f5f4994d8420', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '73ff7cfe-ef3d-401b-9124-0b53ba4c2569', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'bfbf94d1-206f-49a1-873b-817a60422e49', '5') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'bb0b847b-197e-4032-8a96-ef758bbcb76c', '4') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '9e61a042-7015-4af2-a132-802489c7ec1b', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '992ba03d-5239-4a0a-8443-ba548b6e6431', '4') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '12627d49-1ed7-492a-a188-6ef25ac4dbef', '2') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '100d947e-2ec2-4b89-ad92-e80603a7bbd3', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '7f576a1d-4807-4ddd-9f8f-1e22d56e226b', '2') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'aecfba78-2cd3-4090-8294-de3b4360b9d4', '5') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '6adb8628-4591-44b3-ae48-7469e75e314c', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'db579a4a-c13c-400c-8cf7-0b38ed0d7aeb', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '40f33e98-21c8-4bcc-a976-d7254ff12dbd', '3') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '91e390ea-897d-4378-b14b-0c130b0255b8', '6') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '038002d8-e5cb-4335-9c17-d2a31572a8e1', '3') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '46a35e5d-7756-4730-8476-ee86032f137e', '3') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '879fcda7-dff0-488d-9877-ad37de3c43b8', '5') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'c7d9ad75-622f-4759-bfaa-2d9e9148d1f3', '5') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '750de3e9-d0a2-4abf-863f-cb6c4ad39e0f', '5') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'c85849f3-4b68-4f00-b905-3d2979d22e80', '1') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '1cd55c47-b212-4589-856f-fdf143f34b88', '3') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'b93f58f1-67a5-41d6-8100-4234061e5689', '3') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '28873670-b713-4ea9-814c-e3ecdf107cf4', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '94e706b2-d6db-4414-aa78-94cc95117fc9', '5') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'def9db92-aaf5-4696-bef1-6ed3949d3264', '5') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '4f6c9f8b-fd04-4176-b618-901b24730a23', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '280a43a3-6168-46a8-912c-86647bbf08f7', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '0b4624a3-aaf3-4415-81e2-304db4422645', '5') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '555ada22-a0bd-4ba1-9aa3-cf63a100de38', '3') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'c8170052-4a94-454f-bc92-2471721ec4bf', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '4f527130-7c02-499b-81d8-81f2c2c49705', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '028eed0e-5e6c-4bdf-a4f7-fd1070c9de4d', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'c89fd872-a33c-4983-a6b5-172cd6148d96', '3') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '04bf538c-b8db-41d8-a1f8-fe175d4cd0ef', '3') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'd2bc6f27-1cec-4e2b-a362-6f7f6080a8da', '1') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '26c362cf-bea7-4ead-9fd6-19200956cd63', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'c2111c79-3c27-4b92-b928-50086d0beebe', '4') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '812bb368-e39f-4988-9764-8656d9cf59b8', '3') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'c50c4e4d-7cdb-44b0-b9ec-62ea48c94a41', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '9013d1b3-a273-404b-8000-d7440a5dfc43', '4') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '133fdc79-6981-4481-95fa-218688bf5b32', '4') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '9abc800f-cc69-46d1-843a-6b5d6226eaf0', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'f0b42345-650e-4e86-8554-c030f2b9d023', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'b0f40f48-283f-4f34-bea9-f8e9b912a6b9', '5') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '3ce27e7f-abfc-4102-92df-39fbede6c42f', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '7df55551-db82-46f3-a26e-5f37fa6af3c4', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'b8689954-f746-4f5e-b83e-ecdb0cec5f4a', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'a57bc71a-cf4c-41d7-9793-bc630794533e', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '201ade80-031a-43d5-ba10-dc86312084f6', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '1f9bee54-60e5-4780-9a90-6a0bd565b873', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'a2d4a9a5-a172-49d4-b75c-2bee72194531', '0') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '592f7684-30e9-4125-b5b0-877e613bde7a', '7') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '3a15e85e-74f3-4ff2-98d2-61c19bab07eb', '5') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'a8a1af03-e325-453f-83d5-1f53d817b3fb', '5') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '9247327a-a523-461e-8f0a-cf7de5bd6ee9', '2') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '32d0e1a2-466c-4cff-aea5-7828e75b4eef', '4') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'ef8e2c74-b58e-4ae2-9b6a-cb16f26194d9', '10') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '0c020c36-40de-4b25-9900-820873be8119', '550') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', 'f5097bc7-c9f9-44e8-a292-075f2f9d2047', '300') ,
                        ('a573af5b-b4cb-4677-bc19-2dd566d0b1e1', '93606340-6b27-48f8-8109-07d657421834', '450') ,
                                
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'b4a901a7-074b-4ad1-bf0a-7c076849f2db', 10),
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '2f2857c7-4bfc-4f05-aeb9-4bf5900023ed', '30') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '149a395a-de3d-44e7-b87d-92c73eccc416', '50') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '9b909ee6-0329-4e44-bbe6-59231bdf070a', '30') , 
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '45788a7a-526b-4ee8-bf76-489f6f6eaeb6', '50') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'fb461e7d-daa9-4128-91c3-bbb6cfa84be1', '70') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '47fe3e3d-35a6-462e-b1ef-ccfe397710d6', '1') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '3ac6996a-0912-4c46-8867-ce2480465858', '2') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '34fc6bef-5849-4f68-86e6-e4d8b969873e', '2') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '2687cc9f-3a9a-46ec-9d29-4df4bd096c4a', '2') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '8b21d504-b8a7-411c-bf68-960e838df870', '2') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'd9e889d7-d6f5-41dc-a7f2-bdd495a7d1b5', '2') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '9ceb34d6-af2e-4e29-ab8d-6e5ee5a071bb', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '0245fdfd-5780-44eb-9a88-23fdbd00e038', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'bdc6a3f8-3f53-4781-abb3-f5f4994d8420', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '73ff7cfe-ef3d-401b-9124-0b53ba4c2569', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'bfbf94d1-206f-49a1-873b-817a60422e49', '5') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'bb0b847b-197e-4032-8a96-ef758bbcb76c', '4') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '9e61a042-7015-4af2-a132-802489c7ec1b', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '992ba03d-5239-4a0a-8443-ba548b6e6431', '4') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '12627d49-1ed7-492a-a188-6ef25ac4dbef', '2') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '100d947e-2ec2-4b89-ad92-e80603a7bbd3', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '7f576a1d-4807-4ddd-9f8f-1e22d56e226b', '2') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'aecfba78-2cd3-4090-8294-de3b4360b9d4', '5') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '6adb8628-4591-44b3-ae48-7469e75e314c', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'db579a4a-c13c-400c-8cf7-0b38ed0d7aeb', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '40f33e98-21c8-4bcc-a976-d7254ff12dbd', '3') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '91e390ea-897d-4378-b14b-0c130b0255b8', '6') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '038002d8-e5cb-4335-9c17-d2a31572a8e1', '3') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '46a35e5d-7756-4730-8476-ee86032f137e', '3') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '879fcda7-dff0-488d-9877-ad37de3c43b8', '5') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'c7d9ad75-622f-4759-bfaa-2d9e9148d1f3', '5') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '750de3e9-d0a2-4abf-863f-cb6c4ad39e0f', '5') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'c85849f3-4b68-4f00-b905-3d2979d22e80', '1') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '1cd55c47-b212-4589-856f-fdf143f34b88', '3') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'b93f58f1-67a5-41d6-8100-4234061e5689', '3') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '28873670-b713-4ea9-814c-e3ecdf107cf4', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '94e706b2-d6db-4414-aa78-94cc95117fc9', '5') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'def9db92-aaf5-4696-bef1-6ed3949d3264', '5') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '4f6c9f8b-fd04-4176-b618-901b24730a23', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '280a43a3-6168-46a8-912c-86647bbf08f7', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '0b4624a3-aaf3-4415-81e2-304db4422645', '5') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '555ada22-a0bd-4ba1-9aa3-cf63a100de38', '3') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'c8170052-4a94-454f-bc92-2471721ec4bf', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '4f527130-7c02-499b-81d8-81f2c2c49705', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '028eed0e-5e6c-4bdf-a4f7-fd1070c9de4d', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'c89fd872-a33c-4983-a6b5-172cd6148d96', '3') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '04bf538c-b8db-41d8-a1f8-fe175d4cd0ef', '3') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'd2bc6f27-1cec-4e2b-a362-6f7f6080a8da', '1') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '26c362cf-bea7-4ead-9fd6-19200956cd63', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'c2111c79-3c27-4b92-b928-50086d0beebe', '4') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '812bb368-e39f-4988-9764-8656d9cf59b8', '3') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'c50c4e4d-7cdb-44b0-b9ec-62ea48c94a41', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '9013d1b3-a273-404b-8000-d7440a5dfc43', '4') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '133fdc79-6981-4481-95fa-218688bf5b32', '4') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '9abc800f-cc69-46d1-843a-6b5d6226eaf0', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'f0b42345-650e-4e86-8554-c030f2b9d023', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'b0f40f48-283f-4f34-bea9-f8e9b912a6b9', '5') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '3ce27e7f-abfc-4102-92df-39fbede6c42f', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '7df55551-db82-46f3-a26e-5f37fa6af3c4', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'b8689954-f746-4f5e-b83e-ecdb0cec5f4a', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'a57bc71a-cf4c-41d7-9793-bc630794533e', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '201ade80-031a-43d5-ba10-dc86312084f6', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '1f9bee54-60e5-4780-9a90-6a0bd565b873', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'a2d4a9a5-a172-49d4-b75c-2bee72194531', '0') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '592f7684-30e9-4125-b5b0-877e613bde7a', '7') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '3a15e85e-74f3-4ff2-98d2-61c19bab07eb', '5') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'a8a1af03-e325-453f-83d5-1f53d817b3fb', '5') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '9247327a-a523-461e-8f0a-cf7de5bd6ee9', '2') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '32d0e1a2-466c-4cff-aea5-7828e75b4eef', '4') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc, 'ef8e2c74-b58e-4ae2-9b6a-cb16f26194d9', '10') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '0c020c36-40de-4b25-9900-820873be8119', '550') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', 'f5097bc7-c9f9-44e8-a292-075f2f9d2047', '300') ,
                        ('429845a9-92f8-421e-8836-d9f77e8a33cc', '93606340-6b27-48f8-8109-07d657421834', '450') ,
                    ;
                """;
*/
      //  try (PreparedStatement stmt = connection.prepareStatement(sqlCampo)) {
            // Inserção dos campos
            /*
            @Override
    public void addTamanhoApp() {
        perfil.adicionarTamanhoApp("pequeno", 10);
        perfil.adicionarTamanhoApp("médio", 30);
        perfil.adicionarTamanhoApp("grande", 50);
    }

    @Override
    public void addNivelUI() {
        perfil.adicionarNivelUI("mvp", 30);
        perfil.adicionarNivelUI("básico", 50);
        perfil.adicionarNivelUI("profissional", 70);
    }

    @Override
    public void addFuncionalidades() {
        perfil.adicionarFuncionalidade("Cadastro por E-mail e Senha", 1);
        perfil.adicionarFuncionalidade("Cadastro pelo Facebook", 2);
        perfil.adicionarFuncionalidade("Cadastro pelo Twitter", 2);
        perfil.adicionarFuncionalidade("Cadastro pelo Google", 2);
        perfil.adicionarFuncionalidade("Cadastro pelo LinkedIn", 2);
        perfil.adicionarFuncionalidade("Cadastro pelo Github", 2);
        perfil.adicionarFuncionalidade("E-mails de Convite de Usuário", 0);
        perfil.adicionarFuncionalidade("Contas Multi-tenant", 0);
        perfil.adicionarFuncionalidade("Subdomínios", 0);
        perfil.adicionarFuncionalidade("Domínios Personalizados", 0);
        perfil.adicionarFuncionalidade("Painel (Dashboard)", 5);
        perfil.adicionarFuncionalidade("Feed de Atividades (Activity Feed)", 4);
        perfil.adicionarFuncionalidade("Upload de Arquivos (File Uploading)", 0);
        perfil.adicionarFuncionalidade("Upload de Mídia (Media Uploading)", 4);
        perfil.adicionarFuncionalidade("Perfis de Usuário (User Profiles)", 2);
        perfil.adicionarFuncionalidade("E-mails Transacionais (Transactional Emails)", 0);
        perfil.adicionarFuncionalidade("Tags", 2);
        perfil.adicionarFuncionalidade("Avaliações ou Comentários (Ratings or Reviews)", 5);
        perfil.adicionarFuncionalidade("Processamento de Áudio/Vídeo", 0);
        perfil.adicionarFuncionalidade("Pesquisa de Texto Livre (Free text searching)", 0);
        perfil.adicionarFuncionalidade("Pesquisa (Searching)", 3);
        perfil.adicionarFuncionalidade("Calendário (Calendaring)", 6);
        perfil.adicionarFuncionalidade("Exibição de Dados de Mapa / Geolocalização", 3);
        perfil.adicionarFuncionalidade("Exibição de Marcadores/Regiões de Mapa Personalizados", 3);
        perfil.adicionarFuncionalidade("Reservas (Bookings)", 5);
        perfil.adicionarFuncionalidade("Mensagens (Messaging)", 5);
        perfil.adicionarFuncionalidade("Fóruns ou Comentários", 5);
        perfil.adicionarFuncionalidade("Compartilhamento Social (Social Sharing)", 1);
        perfil.adicionarFuncionalidade("Integração com Facebook Open Graph (Push to Facebook Open Graph)", 3);
        perfil.adicionarFuncionalidade("Notificações Push (Push Notifications)", 3);
        perfil.adicionarFuncionalidade("Planos de Assinatura (Subscription plans)", 0);
        perfil.adicionarFuncionalidade("Processamento de Pagamentos (Payment processing)", 5);
        perfil.adicionarFuncionalidade("Carrinho de Compras (Shopping Cart)", 5);
        perfil.adicionarFuncionalidade("Marketplace de Usuários (User Marketplace)", 0);
        perfil.adicionarFuncionalidade("Gerenciamento de Produtos (Product Management)", 0);
        perfil.adicionarFuncionalidade("Compras dentro do Aplicativo (In-App Purchasing)", 5);
        perfil.adicionarFuncionalidade("Coleta de Informações de Pagamento", 3);
        perfil.adicionarFuncionalidade("Integração com CMS (CMS Integration)", 0);
        perfil.adicionarFuncionalidade("Páginas de Administração de Usuários (User Admin pages)", 0);
        perfil.adicionarFuncionalidade("Moderação / Aprovação de Conteúdo (Moderation / Content Approval)", 0);
        perfil.adicionarFuncionalidade("Intercom", 3);
        perfil.adicionarFuncionalidade("Análises de Uso (Usage Analytics)", 3);
        perfil.adicionarFuncionalidade("Relatório de Erros (Crash Reporting)", 1);
        perfil.adicionarFuncionalidade("Monitoramento de Performance (Performance Monitoring)", 0);
        perfil.adicionarFuncionalidade("Suporte Multilíngue (Multilingual Support)", 4);
        perfil.adicionarFuncionalidade("Conectar a um ou mais serviços de terceiros", 3);
        perfil.adicionarFuncionalidade("Uma API para que terceiros integrem ao seu app", 0);
        perfil.adicionarFuncionalidade("Envio de SMS (SMS Messaging)", 4);
        perfil.adicionarFuncionalidade("Mascaramento de Número de Telefone (Phone Number Masking)", 4);
        perfil.adicionarFuncionalidade("Segurança baseada em Certificado SSL", 0);
        perfil.adicionarFuncionalidade("Proteção contra DoS (DoS protection)", 0);
        perfil.adicionarFuncionalidade("Autenticação em Duas Etapas (Two Factor Authentication)", 5);
        perfil.adicionarFuncionalidade("Desenvolvimento Específico do Aplicativo", 0);
        perfil.adicionarFuncionalidade("Design de Ícone do App (App Icon Design)", 0);
        perfil.adicionarFuncionalidade("Sincronização em Nuvem (Cloud Syncing)", 0);
        perfil.adicionarFuncionalidade("Dados de Sensores do Dispositivo (Device Sensor Data)", 0);
        perfil.adicionarFuncionalidade("Códigos de Barras ou QR Codes (Barcodes ou QR Codes)", 0);
        perfil.adicionarFuncionalidade("Dados de Saúde (Health Data)", 0);
        perfil.adicionarFuncionalidade("Apple Watch", 0);
        perfil.adicionarFuncionalidade("Design de Ícone do App (App Icon Design)", 7);
        perfil.adicionarFuncionalidade("Sincronização em Nuvem (Cloud Syncing)", 5);
        perfil.adicionarFuncionalidade("Dados de Sensores do Dispositivo (Device Sensor Data)", 5);
        perfil.adicionarFuncionalidade("Códigos de Barras ou QR Codes", 2);
        perfil.adicionarFuncionalidade("Dados de Saúde (Health Data)", 4);
        perfil.adicionarFuncionalidade("Gerente de Projeto (Project Manager)", 10);
    }

    @Override
    public void addTaxasDiarias() {
        perfil.adicionarTaxaDiaria("designer ui/ux", 550);
        perfil.adicionarTaxaDiaria("gerência de projeto", 300);
        perfil.adicionarTaxaDiaria("desenvolvimento", 450);
    }
             */
//        };

//        for (String[] campo : campos) {
        //    stmt.setString(1, UUID.randomUUID().toString());
         //   stmt.setString(2, campo[0]);
         //   stmt.setString(3, campo[1]);
         //   stmt.execute();
       // }

   // }
   // catch (SQLException e

    
      //  ) {
           // e.printStackTrace();
   // }

//}
//}
