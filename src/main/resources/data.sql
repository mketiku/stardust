-- Stardust Station Initial Manifest
INSERT INTO docking_bay (bay_code, deck_level, required_fleet_affiliation, is_occupied, version) VALUES ('BAY-3A-01', 3, 'TRADING', false, 0);
INSERT INTO docking_bay (bay_code, deck_level, required_fleet_affiliation, is_occupied, version) VALUES ('BAY-3A-02', 3, 'TRADING', false, 0);
INSERT INTO docking_bay (bay_code, deck_level, required_fleet_affiliation, is_occupied, version) VALUES ('BAY-4B-01', 4, 'SCIENCE', false, 0);
INSERT INTO docking_bay (bay_code, deck_level, required_fleet_affiliation, is_occupied, version) VALUES ('BAY-4B-02', 4, 'SCIENCE', false, 0);
INSERT INTO docking_bay (bay_code, deck_level, required_fleet_affiliation, is_occupied, version) VALUES ('BAY-5C-01', 5, 'MILITARY', false, 0);

INSERT INTO starship (registry_name, fleet_affiliation) VALUES ('USS Discovery (NCC-1031)', 'SCIENCE');
INSERT INTO starship (registry_name, fleet_affiliation) VALUES ('Millennium Falcon', 'TRADING');
INSERT INTO starship (registry_name, fleet_affiliation) VALUES ('Rocinante', 'MILITARY');
