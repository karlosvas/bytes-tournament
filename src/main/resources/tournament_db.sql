--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4

-- Started on 2025-06-19 16:02:47

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
-- SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

-- --
-- -- TOC entry 859 (class 1247 OID 16408)
-- -- Name: match_result; Type: TYPE; Schema: public; Owner: postgres

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 220 (class 1259 OID 16435)
-- Name: matches; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.matches (
    id BIGSERIAL NOT NULL,
    tournament_id BIGINT NOT NULL,
    player1_id BIGINT,
    player2_id BIGINT,
    result varchar(20) check (result IN ('PLAYER1_WIN', 'PLAYER2_WIN', 'DRAW', 'PENDING')) NOT NULL,
    round integer NOT NULL
);


ALTER TABLE public.matches OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16455)
-- Name: messages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.messages (
    id BIGSERIAL NOT NULL,
    sender_id BIGINT NOT NULL,
    content text NOT NULL,
    "timestamp" timestamp with time zone NOT NULL,
    match_id BIGINT,
    tournament_id BIGINT
);


ALTER TABLE public.messages OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16420)
-- Name: tournament_players; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tournament_players (
    tournament_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL
);

ALTER TABLE public.tournament_players OWNER TO postgres;


--
-- TOC entry 218 (class 1259 OID 16415)
-- Name: tournaments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tournaments (
    id BIGSERIAL NOT NULL,
    name character varying(100) NOT NULL,
    max_players integer NOT NULL,
    status varchar(20) check (status IN ('PENDING', 'IN_PROGRESS', 'FINISHED')) NOT NULL,
    rounds integer NOT NULL DEFAULT 0,
    max_rounds integer NOT NULL DEFAULT 0
);


ALTER TABLE public.tournaments OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 16395)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    username character varying(50) NOT NULL unique,
    email character varying(100) NOT NULL unique,
    password character varying(255) NOT NULL,
    role varchar(20) check (role IN ('ADMIN', 'PLAYER')) NOT NULL DEFAULT 'PLAYER',
    rank character varying(50) NOT NULL,
    points integer NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

CREATE TABLE public.user_authority_privilegies (
    user_id BIGINT NOT NULL,
    authority_privilegies VARCHAR(255) check (authority_privilegies IN ('CREATE_TOURNAMENT', 'JOIN_TOURNAMENT', 'START_MATCH', 'SEND_MESSAGE', 'VIEW_MATCHES', 'VIEW_TOURNAMENTS', 'MANAGE_USERS')) NOT NULL,
    CONSTRAINT fk_user_auth_priv FOREIGN KEY (user_id) REFERENCES public.users(id),
    PRIMARY KEY (user_id, authority_privilegies)
);

ALTER TABLE public.user_authority_privilegies OWNER TO postgres;

--
-- TOC entry 4933 (class 0 OID 16435)
-- Dependencies: 220
-- Data for Name: matches; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.matches (id, tournament_id, player1_id, player2_id, result, round) FROM stdin;
\.


--
-- TOC entry 4934 (class 0 OID 16455)
-- Dependencies: 221
-- Data for Name: messages; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.messages (id, sender_id, content, "timestamp", match_id, tournament_id) FROM stdin;
\.


--
-- TOC entry 4932 (class 0 OID 16420)
-- Dependencies: 219
-- Data for Name: tournament_players; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tournament_players (tournament_id, user_id) FROM stdin;
\.


--
-- TOC entry 4931 (class 0 OID 16415)
-- Dependencies: 218
-- Data for Name: tournaments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tournaments (id, name, max_players, status, rounds, max_rounds) FROM stdin;
\.


--
-- TOC entry 4930 (class 0 OID 16395)
-- Dependencies: 217
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (role, id, username, password, email, rank, points) FROM stdin;
\.


--
-- TOC entry 4766 (class 2606 OID 16477)
-- Name: messages check_match_or_tournament; Type: CHECK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.messages
    ADD CONSTRAINT check_match_or_tournament CHECK ((((match_id IS NOT NULL) AND (tournament_id IS NULL)) OR ((match_id IS NULL) AND (tournament_id IS NOT NULL)))) NOT VALID;


--
-- TOC entry 4774 (class 2606 OID 16439)
-- Name: matches matches_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.matches
    ADD CONSTRAINT matches_pkey PRIMARY KEY (id);


--
-- TOC entry 4776 (class 2606 OID 16461)
-- Name: messages messages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (id);


--
-- TOC entry 4772 (class 2606 OID 16424)
-- Name: tournament_players tournament_players_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tournament_players
    ADD CONSTRAINT tournament_players_pkey PRIMARY KEY (tournament_id, user_id);


--
-- TOC entry 4770 (class 2606 OID 16419)
-- Name: tournaments tournaments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tournaments
    ADD CONSTRAINT tournaments_pkey PRIMARY KEY (id);


--
-- TOC entry 4768 (class 2606 OID 16399)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 4782 (class 2606 OID 16467)
-- Name: messages fk_match_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT fk_match_id FOREIGN KEY (match_id) REFERENCES public.matches(id) ON DELETE CASCADE;


--
-- TOC entry 4779 (class 2606 OID 16445)
-- Name: matches fk_match_player1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.matches
    ADD CONSTRAINT fk_match_player1 FOREIGN KEY (player1_id) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- TOC entry 4780 (class 2606 OID 16450)
-- Name: matches fk_match_player2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.matches
    ADD CONSTRAINT fk_match_player2 FOREIGN KEY (player2_id) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- TOC entry 4781 (class 2606 OID 16440)
-- Name: matches fk_match_tournament; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.matches
    ADD CONSTRAINT fk_match_tournament FOREIGN KEY (tournament_id) REFERENCES public.tournaments(id) ON DELETE CASCADE;


--
-- TOC entry 4783 (class 2606 OID 16462)
-- Name: messages fk_sender_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT fk_sender_id FOREIGN KEY (sender_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4784 (class 2606 OID 16472)
-- Name: messages fk_tournament_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT fk_tournament_id FOREIGN KEY (tournament_id) REFERENCES public.tournaments(id) ON DELETE CASCADE;


--
-- TOC entry 4777 (class 2606 OID 16425)
-- Name: tournament_players tournament_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tournament_players
    ADD CONSTRAINT tournament_id FOREIGN KEY (tournament_id) REFERENCES public.tournaments(id) ON DELETE CASCADE;


--
-- TOC entry 4778 (class 2606 OID 16430)
-- Name: tournament_players user_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tournament_players
    ADD CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;

--
-- PostgreSQL database dump complete
--

