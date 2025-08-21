CREATE TABLE IF NOT EXISTS users (
  id UUID PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  email VARCHAR(120) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TYPE relationship AS ENUM
('PAI','MAE','FILHO','FILHA','CONJUGE','IRMAO','IRMA','AMIGO','OUTRO');

CREATE TABLE IF NOT EXISTS contacts (
  id UUID PRIMARY KEY,
  user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  first_name VARCHAR(80) NOT NULL,
  last_name VARCHAR(120) NOT NULL,
  birth_date DATE NOT NULL,
  relationship relationship,
  created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS phones (
  id UUID PRIMARY KEY,
  contact_id UUID NOT NULL REFERENCES contacts(id) ON DELETE CASCADE,
  number VARCHAR(25) NOT NULL,
  label VARCHAR(30) -- ex: "celular", "casa"
);

CREATE INDEX IF NOT EXISTS idx_contacts_user ON contacts(user_id);
CREATE INDEX IF NOT EXISTS idx_phones_contact ON phones(contact_id);
